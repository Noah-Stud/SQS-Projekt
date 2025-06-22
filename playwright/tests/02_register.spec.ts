import { test, expect } from '@playwright/test';

test('check register page', async ({ page }) => {
    await page.goto('/#/register');

    // Check if the page title is correct
    //await expect(page.getByText('Register')).toBeVisible();
    await expect(page.getByText('Please enter a valid email')).toBeVisible();
    await expect(page.getByText('Please enter your password')).toBeVisible();
});

test('should register', async ({ page }) => {
    await page.goto('/#/register');

    await page.getByLabel('email').fill('playwright@mail.de');
    await page.getByLabel('password').fill('12345');
    await page.locator('button:text("Register")').click();

    await expect(page).toHaveURL('/#/login');
});

test('should not register', async ({ page }) => {
    await page.goto('/#/register');

    await page.getByLabel('email').fill('playwright@mail.de');
    await page.getByLabel('password').fill('12345');
    await page.locator('button:text("Register")').click();

    await expect(page).toHaveURL('/#/register');
});

test('check login page', async ({ page }) => {
    await page.goto('/#/login');

    //await expect(page.getByText('Log In')).toBeVisible();
    await expect(page.getByText('Please enter a valid email')).toBeVisible();
    await expect(page.getByText('Please enter your password')).toBeVisible();
});

test('should login', async ({ page }) => {
    await page.goto('/#/login');

    await page.getByLabel('email').fill('playwright@mail.de');
    await page.getByLabel('password').fill('12345');
    await page.locator('button:text("Log In")').click();

    await expect(page).toHaveURL('/#/newsfeed');
});

test('should not login', async ({ page }) => {
    await page.goto('/#/login');

    await page.getByLabel('email').fill('playwright@mail.de');
    await page.getByLabel('password').fill('12345');
    await page.locator('button:text("Log In")').click();

    await expect(page).toHaveURL('/#/login');
});