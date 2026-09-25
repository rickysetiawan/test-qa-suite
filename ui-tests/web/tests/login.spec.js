// @ts-check
import { test, expect } from '@playwright/test';
import { LoginPage } from '../src/pages/LoginPage.js';
import { InventoryPage } from '../src/pages/InventoryPage.js';
import { users, errorMessages } from '../src/data/users.js';

test.describe('Login', () => {
  /** @type {LoginPage} */
  let loginPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    await loginPage.goto();
  });

  test('valid user lands on the inventory page', async ({ page }) => {
    const inventoryPage = new InventoryPage(page);
    await loginPage.login(users.standard.username, users.standard.password);

    await expect(page).toHaveURL(/inventory\.html/);
    await expect(inventoryPage.title).toHaveText('Products');
  });

  test('locked out user sees an error', async () => {
    await loginPage.login(users.lockedOut.username, users.lockedOut.password);

    await expect(loginPage.errorMessage).toHaveText(errorMessages.lockedOut);
  });

  test('invalid credentials show an error', async () => {
    await loginPage.login(users.invalid.username, users.invalid.password);

    await expect(loginPage.errorMessage).toHaveText(errorMessages.invalidCredentials);
  });

  test('missing username shows an error', async () => {
    await loginPage.login('', users.standard.password);

    await expect(loginPage.errorMessage).toHaveText(errorMessages.usernameRequired);
  });

  test('missing password shows an error', async () => {
    await loginPage.login(users.standard.username, '');

    await expect(loginPage.errorMessage).toHaveText(errorMessages.passwordRequired);
  });
});
