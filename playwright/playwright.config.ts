import { defineConfig, devices } from '@playwright/test';


export default defineConfig({
    testDir: './tests',
    timeout: 30000,
    fullyParallel: false,
    use: {
        baseURL: 'http://localhost:3000',
        headless: true,
        viewport: { width: 1280, height: 720 },
    },
    webServer: undefined, // Anwendung wird manuell gestartet
});