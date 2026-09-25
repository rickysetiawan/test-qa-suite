// @ts-check
import { test, expect } from '@playwright/test';
import { LoginPage } from '../src/pages/LoginPage.js';
import { InventoryPage } from '../src/pages/InventoryPage.js';
import { users } from '../src/data/users.js';

test.describe('Inventory', () => {
  /** @type {InventoryPage} */
  let inventoryPage;

  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(users.standard.username, users.standard.password);
    inventoryPage = new InventoryPage(page);
  });

  test('sorting by price sorts items low to high', async () => {
    await inventoryPage.sortBy('lohi');

    const prices = await inventoryPage.itemPrices.allTextContents();
    const parsed = prices.map((price) => parseFloat(price.replace('$', '')));
    const sorted = [...parsed].sort((a, b) => a - b);

    expect(parsed).toEqual(sorted);
  });

  test('adding an item to the cart updates the cart badge', async () => {
    await inventoryPage.addItemToCartByName('Sauce Labs Backpack');

    await expect(inventoryPage.cartBadge).toHaveText('1');
  });

  test('removing an item from the cart clears the cart badge', async () => {
    await inventoryPage.addItemToCartByName('Sauce Labs Backpack');
    await inventoryPage.removeItemFromCartByName('Sauce Labs Backpack');

    await expect(inventoryPage.cartBadge).toHaveCount(0);
  });

  test('cart icon navigates to the cart page', async ({ page }) => {
    await inventoryPage.goToCart();

    await expect(page).toHaveURL(/cart\.html/);
  });
});
