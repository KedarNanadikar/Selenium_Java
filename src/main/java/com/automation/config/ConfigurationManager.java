package com.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties
 */
public class ConfigurationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static ConfigurationManager instance;
    private Properties properties;
    
    private ConfigurationManager() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigurationManager
     * @return ConfigurationManager instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.warn("config.properties file not found, using default values");
                setDefaultProperties();
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded successfully from config.properties");
        } catch (IOException e) {
            logger.error("Error loading configuration file", e);
            setDefaultProperties();
        }
    }
    
    /**
     * Set default properties if config file is not found
     */
    private void setDefaultProperties() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("headless", "false");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "30");
        properties.setProperty("page.load.timeout", "30");
        properties.setProperty("base.url", "https://www.google.com");
        properties.setProperty("test.environment", "QA");
        properties.setProperty("screenshot.on.failure", "true");
        properties.setProperty("extent.reports.path", "test-output/ExtentReports/");
        logger.info("Default configuration values set");
    }
    
    /**
     * Get browser name from configuration
     * @return browser name
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Check if headless mode is enabled
     * @return true if headless, false otherwise
     */
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
    
    /**
     * Get implicit wait timeout
     * @return implicit wait in seconds
     */
    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }
    
    /**
     * Get explicit wait timeout
     * @return explicit wait in seconds
     */
    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "30"));
    }
    
    /**
     * Get page load timeout
     * @return page load timeout in seconds
     */
    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }
    
    /**
     * Get base URL for the application
     * @return base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url", "https://www.google.com");
    }
    
    /**
     * Get test environment
     * @return test environment name
     */
    public String getTestEnvironment() {
        return getProperty("test.environment", "QA");
    }
    
    /**
     * Check if screenshot on failure is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }
    
    /**
     * Get ExtentReports output path
     * @return reports directory path
     */
    public String getExtentReportsPath() {
        return getProperty("extent.reports.path", "test-output/ExtentReports/");
    }
    
    /**
     * Get property value with fallback to default
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value
     */
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property value
     * @param key property key
     * @return property value or null if not found
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Get all properties
     * @return Properties object
     */
    public Properties getAllProperties() {
        return (Properties) properties.clone();
    }
}