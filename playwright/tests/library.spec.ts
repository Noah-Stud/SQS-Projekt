import { test, expect } from '@playwright/test';

test('should show page title', async ({ page }) => {
    await page.goto('/');

    // Check if the page title is correct
    await expect(page.getByText('Go to LogInPage')).toBeVisible();
    await expect(page.getByText('Go to RegisterPage')).toBeVisible();
    await expect(page.getByText('Go to NewsFeedPage')).toBeVisible();
});