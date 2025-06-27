import { test, expect } from '@playwright/test';

test('should be newsfeed page', async ({ page }) => {
    await page.goto('/#/newsfeed');

    await expect(page.getByText('NewsFeed')).toBeVisible();
    await expect(page.getByText('Go to Homepage')).toBeVisible();
    await expect(page.getByText('NewsFeed')).toBeVisible();


    await page.goto('/#/register');
    await page.locator('#registerEmail').fill('playwright2@mail.de');
    await page.locator('#registerPassword').fill('12345');
    await page.locator('button:text("Register")').click();


    await page.locator('#logInEmail').fill('playwright2@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();

    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.getByText('NewsFeed')).toBeVisible();
    await expect(page.getByText('Go to Homepage')).toBeVisible();
    await expect(page.getByText('NewsFeed')).toBeVisible();
    await expect(page.getByText('playwright2@mail.de')).toBeVisible();
});


test('should create message', async ({ page }) => {
    await page.goto('/#/login');
    await page.locator('#logInEmail').fill('playwright2@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();
    await expect(page).toHaveURL('/#/newsfeed');

    await page.locator('#inputMessage').fill('f');
    await page.locator('#inputMessage').fill('Test Message for Playwright 42');
    
    var responsePromise = page.waitForResponse(resp => resp.url().includes('/api/message/v1/insert') && resp.status() === 200);
    await page.locator('button:text("Post")').click()
    var response = await responsePromise;

    response = await page.waitForResponse(resp => resp.url().includes('/api/message/v1/getAll') && resp.status() === 200);

    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.getByText('Test+Message+for+Playwright+42')).toBeVisible();
});

test('should not create message', async ({ page }) => {
    await page.goto('/#/newsfeed');
    await page.waitForTimeout(1000);
    await page.locator('#inputMessage').fill('f');
    await page.locator('#inputMessage').fill('Test Message for Playwright (no success)');
    await page.locator('#inputMessage').fill('');
    await expect(page.locator('button:text("Post")')).toBeDisabled()

    await page.waitForTimeout(1000);
    await expect(page.getByText('Test Message for Playwright (no success)')).toHaveCount(0);
    await expect(page).toHaveURL('/#/newsfeed');
});


test('should like message', async ({ page }) => {
    await page.goto('/#/login');
    await page.locator('#logInEmail').fill('playwright2@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();
    await expect(page).toHaveURL('/#/newsfeed');

    var responsePromise = page.waitForResponse(resp => resp.url().includes('/api/message/v1/like') && resp.status() === 200);
    await page.locator('#buttonToggleLike').click()
    await responsePromise;

    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.locator('#amountOfLikes')).toContainText('1', { exact: true });


    await page.locator('#buttonToggleLike').click()
    await responsePromise;

    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.locator('#amountOfLikes')).toContainText('0', { exact: true });
});

test('should not like message', async ({ page }) => {
    await page.goto('/#/newsfeed');
    page.locator('#buttonToggleLike').click()

    await page.waitForTimeout(1000);
    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.locator('#amountOfLikes')).toContainText('0', { exact: true });
});


test('should create new comment', async ({ page }) => {
    await page.goto('/#/login');
    await page.locator('#logInEmail').fill('playwright2@mail.de');
    await page.locator('#logInPassword').fill('12345');
    await page.locator('button:text("Log In")').click();
    await expect(page).toHaveURL('/#/newsfeed');

    await page.locator('#buttonToggleComment').click()
    await page.locator('#inputComment').pressSequentially('Test Comment for Playwright 42');

    var responsePromise// = page.waitForResponse(resp => resp.url().includes('/api/comment/v1/create') && resp.status() === 200);
    await page.locator('#buttonSendComment').click()
    var response// = await responsePromise;

    response = await page.waitForResponse(resp => resp.url().includes('/api/message/v1/getAll') && resp.status() === 200);
    await page.locator('#buttonToggleComment').click()
    await expect(page).toHaveURL('/#/newsfeed');
    await expect(page.getByText('Test Comment for Playwright 42')).toBeVisible();
});

test('should not create comment', async ({ page }) => {
    await page.goto('/#/newsfeed');
    await page.locator('#buttonToggleComment').click()
    await page.locator('#inputComment').pressSequentially('Test Comment for Playwright (no success)');
    await expect(page.locator('#buttonSendComment')).toBeDisabled()

    await page.waitForTimeout(1000);
    await expect(page.getByText('Test Comment for Playwright (no success)')).toHaveCount(0);
    await expect(page).toHaveURL('/#/newsfeed');
});