// @ts-check

export class InventoryPage {
  /** @param {import('@playwright/test').Page} page */
  constructor(page) {
    this.page = page;
    this.title = page.locator('.title');
    this.sortDropdown = page.locator('[data-test="product-sort-container"]');
    this.inventoryItems = page.locator('.inventory_item');
    this.itemPrices = page.locator('.inventory_item_price');
    this.cartBadge = page.locator('.shopping_cart_badge');
    this.cartLink = page.locator('.shopping_cart_link');
  }

  itemByName(name) {
    return this.inventoryItems.filter({ hasText: name });
  }

  async addItemToCartByName(name) {
    await this.itemByName(name).getByRole('button', { name: 'Add to cart' }).click();
  }

  async removeItemFromCartByName(name) {
    await this.itemByName(name).getByRole('button', { name: 'Remove' }).click();
  }

  async sortBy(optionValue) {
    await this.sortDropdown.selectOption(optionValue);
  }

  async goToCart() {
    await this.cartLink.click();
  }
}
