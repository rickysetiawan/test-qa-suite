"""
Database-level QA checks.

These sit a layer below the API/UI tests on purpose: an API response can
look correct while the underlying data is quietly wrong (orphaned rows,
a constraint that isn't actually enforced, a total that doesn't reconcile).
Catching that requires querying the database directly, not just asserting
on what the API chooses to show you.

Requires the local Postgres from db/docker-compose.yml:
    docker compose -f db/docker-compose.yml up -d
"""
import psycopg2
import pytest

DB_CONFIG = dict(
    host="localhost",
    port=5432,
    dbname="ecommerce_qa",
    user="qa_user",
    password="qa_password",
)


@pytest.fixture
def conn():
    connection = psycopg2.connect(**DB_CONFIG)
    yield connection
    connection.close()


@pytest.fixture
def cursor(conn):
    cur = conn.cursor()
    yield cur
    cur.close()


def test_seed_data_loaded(cursor):
    cursor.execute("SELECT COUNT(*) FROM products;")
    assert cursor.fetchone()[0] == 5


def test_no_orphaned_order_items(cursor):
    """Every order_item must reference an order and a product that actually exist.
    Foreign keys should already guarantee this — this test verifies the guarantee
    is real rather than assuming the schema was applied correctly."""
    cursor.execute("""
        SELECT oi.id FROM order_items oi
        LEFT JOIN orders o ON oi.order_id = o.id
        WHERE o.id IS NULL;
    """)
    assert cursor.fetchall() == [], "Found order_items pointing at a non-existent order"

    cursor.execute("""
        SELECT oi.id FROM order_items oi
        LEFT JOIN products p ON oi.product_id = p.id
        WHERE p.id IS NULL;
    """)
    assert cursor.fetchall() == [], "Found order_items pointing at a non-existent product"


def test_order_total_reconciles_with_line_items(cursor):
    """Business-rule check: an order's total should equal the sum of its line items.
    This is the kind of bug that a UI test would never surface if the front end
    and the database happen to agree on the wrong number."""
    cursor.execute("""
        SELECT order_id, SUM(quantity * unit_price_cents) AS computed_total
        FROM order_items
        GROUP BY order_id
        ORDER BY order_id;
    """)
    totals = cursor.fetchall()
    assert totals[0] == (1, 2999 * 1 + 999 * 2)  # order 1: backpack x1 + bike light x2


def test_out_of_stock_product_cannot_be_ordered(cursor):
    """Product 5 (Sauce Labs Onesie) is seeded with stock_qty = 0.
    Confirms the out-of-stock product has no live orders against it —
    a check that would catch a race condition in checkout logic."""
    cursor.execute("SELECT stock_qty FROM products WHERE id = 5;")
    assert cursor.fetchone()[0] == 0

    cursor.execute("SELECT COUNT(*) FROM order_items WHERE product_id = 5;")
    assert cursor.fetchone()[0] == 0


def test_negative_quantity_is_rejected_by_schema(conn, cursor):
    """The CHECK constraint on order_items.quantity should reject bad data
    at the database layer, not rely on the application to catch it first.
    Runs in its own transaction and rolls back so the seed data is untouched."""
    with pytest.raises(psycopg2.errors.CheckViolation):
        cursor.execute("""
            INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents)
            VALUES (1, 1, -3, 2999);
        """)
    conn.rollback()


def test_invalid_order_status_is_rejected_by_schema(conn, cursor):
    """Only a fixed set of statuses should ever be possible for an order."""
    with pytest.raises(psycopg2.errors.CheckViolation):
        cursor.execute("""
            INSERT INTO orders (user_id, status) VALUES (1, 'teleported');
        """)
    conn.rollback()
