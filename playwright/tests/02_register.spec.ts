import { test, expect } from '@playwright/test';

test('should be register page', async ({ page }) => {
    await page.goto('/#/register');

    // Check if the page title is correct
    await expect(page.getByText('Register').first()).toBeVisible();
    await expect(page.getByText('Register')).toHaveCount(2);
    await expect(page.getByText('Please enter a valid email')).toBeVisible();
    await expect(page.getByText('Please enter your password')).toBeVisible();
});

test('should register', async ({ page }) => {
    await page.goto('/#/register');

    await page.locator('#redEId').fill('playwright@mail.de');
    await page.locator('#redPId').fill('12345');
    await page.locator('button:text("Register")').click();

    await expect(page).toHaveURL('/#/login');
});

test('should not register', async ({ page }) => {
    await page.goto('/#/register');

    await page.locator('#redEId').fill('playwright@mail.de');
    await page.locator('#redPId').fill('12345');
    await page.locator('button:text("Register")').click();

    await expect(page).toHaveURL('/#/register');
});


test('should be login page', async ({ page }) => {
    await page.goto('/#/login');

    await expect(page.getByText('Log In').first()).toBeVisible();
    await expect(page.getByText('Log In')).toHaveCount(2);
    await expect(page.getByText('Please enter a valid email')).toBeVisible();
    await expect(page.getByText('Please enter your password')).toBeVisible();
});

test('should login', async ({ page }) => {
    await page.goto('/#/login');

    await page.locator('#logInEmail').fill('playwright@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();

    await expect(page).toHaveURL('/#/newsfeed');
});

test('should not login', async ({ page }) => {
    await page.goto('/#/login');

    await page.locator('#logInEmail').fill('playwright@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();

    await expect(page).toHaveURL('/#/login');
});