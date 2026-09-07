-- Minimal e-commerce schema for DB-level QA checks: enough referential
-- structure (FKs, NOT NULL, CHECK constraints) to make integrity tests
-- meaningful, without pretending this is a production data model.

CREATE TABLE users (
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    full_name     VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE products (
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    price_cents   INTEGER NOT NULL CHECK (price_cents >= 0),
    category      VARCHAR(100) NOT NULL,
    stock_qty     INTEGER NOT NULL DEFAULT 0 CHECK (stock_qty >= 0)
);

CREATE TABLE orders (
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER NOT NULL REFERENCES users(id),
    status        VARCHAR(20) NOT NULL DEFAULT 'pending'
                  CHECK (status IN ('pending', 'paid', 'shipped', 'cancelled')),
    created_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE order_items (
    id            SERIAL PRIMARY KEY,
    order_id      INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id    INTEGER NOT NULL REFERENCES products(id),
    quantity      INTEGER NOT NULL CHECK (quantity > 0),
    unit_price_cents INTEGER NOT NULL CHECK (unit_price_cents >= 0)
);

-- Seed data: 3 users, 5 products, 2 orders (one clean, one used by tests
-- to prove a broken-data scenario gets caught)
INSERT INTO users (email, full_name) VALUES
    ('ricky@example.com', 'Ricky Setiawan'),
    ('buyer2@example.com', 'Test Buyer Two'),
    ('buyer3@example.com', 'Test Buyer Three');

INSERT INTO products (title, price_cents, category, stock_qty) VALUES
    ('Sauce Labs Backpack', 2999, 'bags', 50),
    ('Sauce Labs Bike Light', 999, 'accessories', 120),
    ('Sauce Labs Bolt T-Shirt', 1599, 'apparel', 75),
    ('Sauce Labs Fleece Jacket', 4999, 'apparel', 30),
    ('Sauce Labs Onesie', 749, 'apparel', 0);  -- intentionally out of stock, used by a test

INSERT INTO orders (user_id, status) VALUES
    (1, 'paid'),
    (2, 'pending');

INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents) VALUES
    (1, 1, 1, 2999),
    (1, 2, 2, 999);
