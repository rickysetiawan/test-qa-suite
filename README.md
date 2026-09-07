# E-Commerce QA Automation Suite

A single, coherent test suite spanning the four layers a senior QA/SDET role
actually touches — UI, API, performance, and data — wired together with CI/CD.
Built as a portfolio project against public demo targets:
[saucedemo.com](https://www.saucedemo.com) (UI) and
[fakestoreapi.com](https://fakestoreapi.com) (API/performance).

I built this to demonstrate the same architecture I use professionally
(Page Object Model UI automation, contract-level API tests, load modeling,
data-integrity checks below the API surface, all gated by CI) without
using proprietary employer code.

## Why one repo instead of five

A real product's quality strategy isn't five disconnected scripts — it's
one strategy expressed across layers that all have to agree with each
other. The DB integrity tests exist because an API test alone can't tell
you the underlying data is wrong; the API tests exist because a UI test
alone is too slow and too brittle to run on every commit; the performance
suite only runs after the API tests pass, because load-testing a broken
endpoint just measures how fast it fails.

## Architecture

| Layer | Stack | What it proves |
|---|---|---|
| `ui-tests/` | Python, Selenium, pytest, Page Object Model | End-to-end user flows (login, checkout) behave correctly, including negative paths |
| `api-tests/` | Java, RestAssured, JUnit 5 | Contract-level correctness, including a cross-resource integrity check (cart line items resolve to real products) |
| `performance/` | Python, Locust | The API holds up under a realistic, weighted traffic mix — not just raw req/s |
| `db/` | PostgreSQL, Docker Compose, psycopg2 | Data integrity and constraint enforcement below the API surface |
| `.github/workflows/` | GitHub Actions | All four layers run on every push, in parallel, with reports as artifacts |

## Running it locally

```bash
# UI tests
cd ui-tests && pip install -r requirements.txt && pytest -m smoke

# API tests
cd api-tests && mvn test

# Performance smoke test (60s, 20 users)
cd performance && pip install -r requirements.txt
locust -f locustfile.py --host https://fakestoreapi.com --headless -u 20 -r 5 -t 60s

# DB integrity tests
cd db && docker compose up -d
pip install -r requirements.txt && pytest -v
```

## Design decisions worth asking me about in an interview

- **Why Page Object Model for UI, not a flat script per test?** Locators
  live in exactly one place, so a UI change means one file changes, not
  every test that touches that page.
- **Why does the cart API test check the *products* endpoint too?**
  Because the bug that actually ships to production is rarely "the cart
  endpoint returns a 500." It's "the cart endpoint returns 200 with a
  `productId` that no longer exists." Single-endpoint tests don't catch that.
- **Why does performance testing depend on the API tests passing first?**
  Load-testing a functionally broken endpoint produces numbers that look
  like a performance problem but are actually a correctness problem —
  wasted signal.
- **Why enforce constraints at the database layer, not just in application
  code?** Application-layer validation can be bypassed by a bad migration,
  a second service writing to the same table, or a bug. The CHECK
  constraints are the last line of defense, and this suite tests that
  they actually hold, not just that the API is polite about rejecting
  bad input.

## About

Ricky Setiawan — Software Test Engineer, 7+ years in test automation,
CI/CD, and quality strategy for e-commerce and IoT platforms.
[LinkedIn](https://linkedin.com/in/setiawan-ricky) ·
[GitHub](https://github.com/rickysetiawan)
