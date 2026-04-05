package com.automation.base;

import com.automation.config.ConfigurationManager;
import com.automation.factory.WebDriverFactory;
import com.automation.listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;

/**
 * Base test class providing common setup and teardown functionality
 */
@Listeners({TestListener.class})
public abstract class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigurationManager config;
    
    /**
     * Setup method executed before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up test environment...");
        
        try {
            // Initialize configuration
            config = ConfigurationManager.getInstance();
            
            // Get browser from system property or configuration
            String browser = System.getProperty("browser", config.getBrowser());
            boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", String.valueOf(config.isHeadless()))
            );
            
            logger.info("Initializing WebDriver - Browser: {}, Headless: {}", browser, headless);
            
            // Create WebDriver instance
            driver = WebDriverFactory.createDriver(browser, headless);
            
            if (driver != null) {
                // Set timeouts
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
                
                logger.info("Test setup completed successfully");
                TestListener.logInfo("Test environment initialized with browser: " + browser);
            } else {
                throw new RuntimeException("Failed to initialize WebDriver");
            }
            
        } catch (Exception e) {
            logger.error("Error during test setup", e);
            TestListener.logFail("Test setup failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Teardown method executed after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down test environment...");
        
        try {
            if (driver != null) {
                WebDriverFactory.closeDriver();
                logger.info("WebDriver closed successfully");
                TestListener.logInfo("Test environment cleaned up");
            }
        } catch (Exception e) {
            logger.error("Error during test teardown", e);
            TestListener.logFail("Test teardown failed: " + e.getMessage());
        }
    }
    
    /**
     * Get current WebDriver instance
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Get configuration instance
     * @return ConfigurationManager instance
     */
    protected ConfigurationManager getConfig() {
        return config;
    }
    
    /**
     * Navigate to base URL
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        logger.info("Navigating to base URL: {}", baseUrl);
        driver.get(baseUrl);
        TestListener.logInfo("Navigated to: " + baseUrl);
    }
    
    /**
     * Navigate to specific URL
     * @param url target URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        TestListener.logInfo("Navigated to: " + url);
    }
    
    /**
     * Get current page title
     * @return page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Current page title: {}", title);
        return title;
    }
    
    /**
     * Get current URL
     * @return current URL
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }
    
    /**
     * Wait for a specified amount of time
     * @param milliseconds time to wait in milliseconds
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted", e);
        }
    }
    
    /**
     * Log test step information
     * @param stepDescription description of the test step
     */
    protected void logTestStep(String stepDescription) {
        logger.info("Test Step: {}", stepDescription);
        TestListener.logInfo(stepDescription);
    }
    
    /**
     * Log test verification
     * @param verification verification description
     * @param passed whether the verification passed
     */
    protected void logVerification(String verification, boolean passed) {
        if (passed) {
            logger.info("✓ Verification PASSED: {}", verification);
            TestListener.logPass("✓ " + verification);
        } else {
            logger.error("✗ Verification FAILED: {}", verification);
            TestListener.logFail("✗ " + verification);
        }
    }
}