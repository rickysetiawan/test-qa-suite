"""
Load test for the e-commerce API layer (FakeStoreAPI).

Task weights approximate real shopper behavior: most traffic is browsing
(list products, drill into one, filter by category), a smaller slice
actually adds something to a cart, and a still smaller slice checks out.
Modeling that mix matters more than raw requests/sec — a flat 1:1:1 task
split would understate how much load your product-listing endpoint
actually needs to absorb relative to checkout.

Run:
    locust -f locustfile.py --host https://fakestoreapi.com
Headless, CI-friendly run (used in the GitHub Actions workflow):
    locust -f locustfile.py --host https://fakestoreapi.com \
        --headless -u 20 -r 5 -t 60s --html report.html
"""
import random
from locust import HttpUser, task, between


class ShopperUser(HttpUser):
    wait_time = between(1, 3)  # seconds between a user's actions — avoids
                                # an unrealistic tight-loop hammering the API

    def on_start(self):
        self.product_ids = list(range(1, 21))  # FakeStoreAPI ships 20 products

    @task(10)
    def browse_all_products(self):
        self.client.get("/products", name="/products [list]")

    @task(6)
    def view_single_product(self):
        product_id = random.choice(self.product_ids)
        self.client.get(f"/products/{product_id}", name="/products/[id]")

    @task(3)
    def browse_category(self):
        category = random.choice(
            ["electronics", "jewelery", "men's clothing", "women's clothing"]
        )
        self.client.get(f"/products/category/{category}", name="/products/category/[category]")

    @task(2)
    def add_to_cart(self):
        payload = {
            "userId": random.randint(1, 10),
            "date": "2026-07-30",
            "products": [{"productId": random.choice(self.product_ids), "quantity": 1}],
        }
        self.client.post("/carts", json=payload, name="/carts [create]")

    @task(1)
    def view_own_cart(self):
        cart_id = random.randint(1, 7)
        self.client.get(f"/carts/{cart_id}", name="/carts/[id]")
