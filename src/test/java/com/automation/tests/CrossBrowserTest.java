package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleSearchPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Cross browser test class to demonstrate testing across different browsers
 */
public class CrossBrowserTest extends BaseTest {
    
    private GoogleSearchPage googlePage;
    
    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupPage() {
        googlePage = new GoogleSearchPage();
    }
    
    @Test
    @Parameters({"testUrl", "searchQuery"})
    public void testCrossBrowserSearch(String testUrl, String searchQuery) {
        logger.info("Starting cross-browser test with URL: {} and query: {}", testUrl, searchQuery);
        
        // Navigate to the test URL
        googlePage.navigateTo(testUrl);
        
        // Verify page loads correctly
        Assert.assertTrue(googlePage.getPageTitle().contains("Google"), 
            "Page should load correctly in current browser");
        
        // Perform search
        googlePage.search(searchQuery);
        
        // Verify search functionality works across browsers
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
            "Search should work correctly in current browser");
        
        int resultsCount = googlePage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0, "Should have search results in current browser");
        
        logger.info("Cross-browser test completed successfully. Results found: {}", resultsCount);
    }
    
    @Test
    public void testBrowserSpecificFeatures() {
        logger.info("Testing browser-specific features");
        
        // Navigate to Google
        googlePage.navigateToGoogle();
        
        // Get browser information
        String userAgent = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return navigator.userAgent;");
        
        logger.info("Running test on browser: {}", userAgent);
        
        // Test basic functionality that should work across all browsers
        googlePage.search("Cross browser testing");
        
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
            "Basic search functionality should work in all browsers");
        
        // Test JavaScript execution (should work in all modern browsers)
        String pageTitle = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return document.title;");
        
        Assert.assertFalse(pageTitle.isEmpty(), "JavaScript execution should work in browser");
        
        logger.info("Browser-specific features test completed for: {}", userAgent);
    }
    
    @Test
    public void testResponsiveDesign() {
        logger.info("Testing responsive design across different viewport sizes");
        
        googlePage.navigateToGoogle();
        
        // Test different viewport sizes (simulating different devices)
        int[][] viewportSizes = {
            {1920, 1080}, // Desktop
            {1366, 768},  // Laptop
            {768, 1024},  // Tablet
            {375, 667}    // Mobile
        };
        
        for (int[] size : viewportSizes) {
            int width = size[0];
            int height = size[1];
            
            // Set viewport size
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
            logger.info("Testing viewport size: {}x{}", width, height);
            
            // Refresh page to apply new viewport
            driver.navigate().refresh();
            googlePage.handleCookiesPopup();
            
            // Verify basic functionality still works
            Assert.assertTrue(googlePage.isGoogleLogoDisplayed(), 
                "Google logo should be visible at viewport " + width + "x" + height);
            
            // Quick search test
            googlePage.search("responsive design");
            Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
                "Search should work at viewport " + width + "x" + height);
            
            // Clear for next iteration
            googlePage.navigateToGoogle();
        }
        
        logger.info("Responsive design test completed successfully");
    }
}