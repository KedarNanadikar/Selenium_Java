package com.automation.base;

import com.automation.config.ConfigurationManager;
import com.automation.factory.WebDriverFactory;
import com.automation.listeners.TestListener;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Base page class providing common page interaction methods
 */
public abstract class BasePage {
    
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigurationManager config;
    
    /**
     * Constructor to initialize BasePage
     */
    public BasePage() {
        this.driver = WebDriverFactory.getDriver();
        this.config = ConfigurationManager.getInstance();
        
        if (driver != null) {
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
            PageFactory.initElements(driver, this);
        } else {
            throw new RuntimeException("WebDriver instance is null. Ensure WebDriver is initialized before creating page objects.");
        }
    }
    
    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return WebElement when visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", element);
            TestListener.logFail("Element not visible: " + element);
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return WebElement when clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: {}", element);
            TestListener.logFail("Element not clickable: " + element);
            throw e;
        }
    }
    
    /**
     * Wait for element to be present
     * @param locator By locator
     * @return WebElement when present
     */
    protected WebElement waitForElementPresent(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not present within timeout: {}", locator);
            TestListener.logFail("Element not present: " + locator);
            throw e;
        }
    }
    
    /**
     * Click on element with wait
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            waitForElementClickable(element);
            element.click();
            logger.info("Clicked on element: {}", element);
            TestListener.logInfo("Clicked on element");
        } catch (Exception e) {
            logger.error("Failed to click element: {}", element, e);
            TestListener.logFail("Failed to click element: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Type text into element
     * @param element WebElement to type into
     * @param text text to type
     */
    protected void type(WebElement element, String text) {
        try {
            waitForElementVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '{}' into element: {}", text, element);
            TestListener.logInfo("Typed text: " + text);
        } catch (Exception e) {
            logger.error("Failed to type into element: {}", element, e);
            TestListener.logFail("Failed to type text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get text from element
     * @param element WebElement to get text from
     * @return element text
     */
    protected String getText(WebElement element) {
        try {
            waitForElementVisible(element);
            String text = element.getText();
            logger.info("Got text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", element, e);
            TestListener.logFail("Failed to get text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return true if displayed, false otherwise
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", element);
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     * @param element WebElement to check
     * @return true if enabled, false otherwise
     */
    protected boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            logger.debug("Element not enabled: {}", element);
            return false;
        }
    }
    
    /**
     * Select option from dropdown by visible text
     * @param element WebElement dropdown
     * @param optionText visible text of option
     */
    protected void selectByText(WebElement element, String optionText) {
        try {
            waitForElementVisible(element);
            Select select = new Select(element);
            select.selectByVisibleText(optionText);
            logger.info("Selected option '{}' from dropdown", optionText);
            TestListener.logInfo("Selected option: " + optionText);
        } catch (Exception e) {
            logger.error("Failed to select option '{}' from dropdown", optionText, e);
            TestListener.logFail("Failed to select option: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Select option from dropdown by value
     * @param element WebElement dropdown
     * @param value value of option
     */
    protected void selectByValue(WebElement element, String value) {
        try {
            waitForElementVisible(element);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Selected option with value '{}' from dropdown", value);
            TestListener.logInfo("Selected option by value: " + value);
        } catch (Exception e) {
            logger.error("Failed to select option with value '{}' from dropdown", value, e);
            TestListener.logFail("Failed to select option by value: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get all options from dropdown
     * @param element WebElement dropdown
     * @return List of option texts
     */
    protected List<String> getDropdownOptions(WebElement element) {
        try {
            waitForElementVisible(element);
            Select select = new Select(element);
            return select.getOptions().stream()
                    .map(WebElement::getText)
                    .toList();
        } catch (Exception e) {
            logger.error("Failed to get dropdown options", e);
            TestListener.logFail("Failed to get dropdown options: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Scroll to element
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500); // Brief pause after scrolling
            logger.info("Scrolled to element: {}", element);
            TestListener.logInfo("Scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", element, e);
            TestListener.logFail("Failed to scroll to element: " + e.getMessage());
        }
    }
    
    /**
     * Execute JavaScript
     * @param script JavaScript code to execute
     * @param args arguments for the script
     * @return result of script execution
     */
    protected Object executeJavaScript(String script, Object... args) {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            Object result = jsExecutor.executeScript(script, args);
            logger.info("Executed JavaScript: {}", script);
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript: {}", script, e);
            TestListener.logFail("Failed to execute JavaScript: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get page title
     * @return page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
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
     * Navigate to URL
     * @param url target URL
     */
    protected void navigateTo(String url) {
        driver.get(url);
        logger.info("Navigated to: {}", url);
        TestListener.logInfo("Navigated to: " + url);
    }
    
    /**
     * Refresh page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
        TestListener.logInfo("Page refreshed");
    }
    
    /**
     * Handle alert and accept
     * @return alert text
     */
    protected String acceptAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            logger.info("Alert accepted with text: {}", alertText);
            TestListener.logInfo("Alert accepted: " + alertText);
            return alertText;
        } catch (Exception e) {
            logger.error("Failed to handle alert", e);
            TestListener.logFail("Failed to handle alert: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Handle alert and dismiss
     * @return alert text
     */
    protected String dismissAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.dismiss();
            logger.info("Alert dismissed with text: {}", alertText);
            TestListener.logInfo("Alert dismissed: " + alertText);
            return alertText;
        } catch (Exception e) {
            logger.error("Failed to handle alert", e);
            TestListener.logFail("Failed to handle alert: " + e.getMessage());
            throw e;
        }
    }
}