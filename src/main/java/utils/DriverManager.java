package utils;

import com.microsoft.playwright.*;

import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

public class DriverManager {

    // ThreadLocal is the key to parallel execution.
    // It creates a separate box for "DriverManager" for every single thread running.
    private static final ThreadLocal<DriverManager> threadLocalDriver = new ThreadLocal<>();
    private final String videoRecordDir = Paths.get(System.getProperty("user.dir"), "resultVideos").toString();
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;

    // Private constructor: Only this class can create itself
    private DriverManager(String browserName) {
        playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(true); // Set true for CI

        switch (browserName.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chrome":
            default:
                launchOptions.setArgs(List.of("--start-maximized"));
                browser = playwright.chromium().launch(launchOptions);
                break;
        }
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions().setRecordVideoDir(Paths.get(videoRecordDir)).setRecordVideoSize(1920,1080);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        if (browserName.equalsIgnoreCase("chromium") || browserName.equalsIgnoreCase("chrome")) {
            contextOptions.setViewportSize(null); // Respects --start-maximized
        } else {
            contextOptions.setViewportSize(width, height); // Manually forces full size for Firefox/Safari
        }

        context = browser.newContext(contextOptions);
        page = context.newPage();
    }

    // --- Static Methods to Access the Driver ---

    public static void init(String browserName) {
        if (threadLocalDriver.get() == null) {
            threadLocalDriver.set(new DriverManager(browserName));
        }
    }

    public static Page getPage() {
        if (threadLocalDriver.get() == null) throw new RuntimeException("Driver not initialized!");
        return threadLocalDriver.get().page;
    }

    public static void quit() {
        if (threadLocalDriver.get() != null) {
            DriverManager manager = threadLocalDriver.get();

            // 1. Close the Browser (Destroys all pages & contexts instantly)
            try {
                if (manager.browser != null) {
                    manager.browser.close();
                }
            } catch (Exception e) {
                // Log warning but don't stop execution
                System.err.println("Warning: Browser close failed: " + e.getMessage());
            }

            // 2. Close the Playwright Server Connection
            try {
                if (manager.playwright != null) {
                    manager.playwright.close();
                }
            } catch (Exception e) {
                System.err.println("Warning: Playwright server close failed: " + e.getMessage());
            }

            // 3. Always clean the ThreadLocal
            threadLocalDriver.remove();
        }
    }
}