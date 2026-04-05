package com.automation.base;

import com.automation.utils.ConfigurationManager;
import com.automation.utils.TestListener;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * Base test class that all test classes should extend
 * Provides common setup and teardown methods
 */
@Listeners(TestListener.class)
public class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigurationManager config;
    
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless) {
        logger.info("Setting up test with browser: {} (headless: {})", browser, headless);
        
        try {
            // Initialize configuration
            config = ConfigurationManager.getInstance();
            
            // Get browser from system property if available, otherwise use parameter
            String browserName = System.getProperty("browser", browser);
            boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", headless));
            
            // Create WebDriver instance
            driver = WebDriverFactory.createDriver(browserName, isHeadless);
            
            // Set implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
            
            logger.info("Test setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to set up test", e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Cleaning up test resources");
        
        try {
            if (driver != null) {
                WebDriverFactory.closeDriver();
                logger.info("Test cleanup completed successfully");
            }
        } catch (Exception e) {
            logger.error("Error during test cleanup", e);
        }
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
    }
    
    /**
     * Get current page title
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current page URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for specified number of seconds
     * @param seconds Number of seconds to wait
     */
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted during wait", e);
        }
    }
}