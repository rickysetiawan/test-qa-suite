// @ts-check
import { test, expect } from '@playwright/test';
import { LoginPage } from '../src/pages/LoginPage.js';
import { InventoryPage } from '../src/pages/InventoryPage.js';
import { CartPage } from '../src/pages/CartPage.js';
import { CheckoutPage } from '../src/pages/CheckoutPage.js';
import { users } from '../src/data/users.js';
import { checkoutInfo } from '../src/data/checkout.js';

test.describe('Checkout', () => {
  /** @type {CheckoutPage} */
  let checkoutPage;

  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(users.standard.username, users.standard.password);

    const inventoryPage = new InventoryPage(page);
    await inventoryPage.addItemToCartByName('Sauce Labs Backpack');
    await inventoryPage.goToCart();

    const cartPage = new CartPage(page);
    await cartPage.checkout();

    checkoutPage = new CheckoutPage(page);
  });

  test('completing checkout info shows the order summary', async ({ page }) => {
    await checkoutPage.fillInfo(checkoutInfo.firstName, checkoutInfo.lastName, checkoutInfo.postalCode);

    await expect(page).toHaveURL(/checkout-step-two\.html/);
    await expect(checkoutPage.summaryTotalLabel).toBeVisible();
  });

  test('finishing checkout shows the confirmation screen', async ({ page }) => {
    await checkoutPage.fillInfo(checkoutInfo.firstName, checkoutInfo.lastName, checkoutInfo.postalCode);
    await checkoutPage.finish();

    await expect(page).toHaveURL(/checkout-complete\.html/);
    await expect(checkoutPage.completeHeader).toHaveText('Thank you for your order!');
  });

  test('missing postal code shows an error', async () => {
    await checkoutPage.fillInfo(checkoutInfo.firstName, checkoutInfo.lastName, '');

    await expect(checkoutPage.errorMessage).toHaveText('Error: Postal Code is required');
  });
});
