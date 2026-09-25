// @ts-check
import { test, expect } from '@playwright/test';
import { LoginPage } from '../src/pages/LoginPage.js';
import { InventoryPage } from '../src/pages/InventoryPage.js';
import { CartPage } from '../src/pages/CartPage.js';
import { users } from '../src/data/users.js';

test.describe('Cart', () => {
  /** @type {InventoryPage} */
  let inventoryPage;
  /** @type {CartPage} */
  let cartPage;

  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(users.standard.username, users.standard.password);

    inventoryPage = new InventoryPage(page);
    cartPage = new CartPage(page);
    await inventoryPage.addItemToCartByName('Sauce Labs Backpack');
    await inventoryPage.addItemToCartByName('Sauce Labs Bike Light');
    await inventoryPage.goToCart();
  });

  test('added items appear in the cart', async () => {
    await expect(cartPage.cartItems).toHaveCount(2);
  });

  test('removing an item from the cart removes it from the list', async () => {
    await cartPage.removeItemByName('Sauce Labs Bike Light');

    await expect(cartPage.cartItems).toHaveCount(1);
    await expect(cartPage.itemByName('Sauce Labs Backpack')).toBeVisible();
  });

  test('checkout button navigates to checkout step one', async ({ page }) => {
    await cartPage.checkout();

    await expect(page).toHaveURL(/checkout-step-one\.html/);
  });
});
