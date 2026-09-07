"""
Shared pytest fixtures for the UI test suite.

Design notes:
- `driver` is function-scoped so every test gets a clean browser session
  (no shared state leaking between tests).
- Headless mode is on by default so the suite runs unattended in CI;
  pass --no-headless locally if you want to watch the browser.
"""
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

BASE_URL = "https://www.saucedemo.com/"


def pytest_addoption(parser):
    parser.addoption(
        "--no-headless",
        action="store_true",
        default=False,
        help="Run with a visible browser window instead of headless.",
    )


@pytest.fixture
def driver(request):
    options = Options()
    if not request.config.getoption("--no-headless"):
        options.add_argument("--headless=new")
    options.add_argument("--window-size=1400,1000")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")

    service = Service(ChromeDriverManager().install())
    drv = webdriver.Chrome(service=service, options=options)
    drv.implicitly_wait(5)

    yield drv

    # Attach a screenshot on failure before closing, so CI runs are debuggable
    if request.node.rep_call.failed if hasattr(request.node, "rep_call") else False:
        drv.save_screenshot(f"failure_{request.node.name}.png")
    drv.quit()


@pytest.hookimpl(tryfirst=True, hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """Expose test outcome to fixtures (used for the failure-screenshot hook above)."""
    outcome = yield
    rep = outcome.get_result()
    setattr(item, "rep_" + rep.when, rep)
