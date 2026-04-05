package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleSearchPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Smoke test class for basic functionality verification
 */
public class SmokeTest extends BaseTest {
    
    private GoogleSearchPage googlePage;
    
    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupPage() {
        googlePage = new GoogleSearchPage();
    }
    
    @Test(priority = 1, groups = {"smoke", "regression"})
    public void smokeTestGoogleHomepage() {
        logger.info("Running smoke test for Google homepage");
        
        googlePage.navigateToGoogle();
        
        // Basic verification that page loads
        String pageTitle = googlePage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Google"), 
            "Google homepage should load with correct title");
        
        // Verify search functionality is available
        Assert.assertTrue(googlePage.isGoogleLogoDisplayed(), 
            "Google logo should be visible");
        
        logger.info("Google homepage smoke test passed");
    }
    
    @Test(priority = 2, groups = {"smoke", "regression"})
    public void smokeTestBasicSearch() {
        logger.info("Running smoke test for basic search functionality");
        
        googlePage.navigateToGoogle();
        googlePage.search("test automation");
        
        // Verify search works
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
            "Search should return results");
        
        Assert.assertTrue(googlePage.getSearchResultsCount() > 0, 
            "Should have at least one search result");
        
        logger.info("Basic search smoke test passed");
    }
    
    @Test(priority = 3, groups = {"smoke"})
    public void smokeTestPageNavigation() {
        logger.info("Running smoke test for page navigation");
        
        googlePage.navigateToGoogle();
        
        // Verify we can navigate and the URL is correct
        String currentUrl = googlePage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("google"), 
            "Should be on Google domain");
        
        // Perform a search and verify URL changes
        googlePage.search("selenium");
        
        String searchUrl = googlePage.getCurrentUrl();
        Assert.assertTrue(searchUrl.contains("search"), 
            "URL should contain 'search' after performing search");
        Assert.assertTrue(searchUrl.contains("selenium"), 
            "URL should contain the search query");
        
        logger.info("Page navigation smoke test passed");
    }
    
    @Test(priority = 4, groups = {"smoke", "api"})
    public void smokeTestJavaScriptExecution() {
        logger.info("Running smoke test for JavaScript execution capability");
        
        googlePage.navigateToGoogle();
        
        // Test JavaScript execution
        Object result = ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return document.readyState;");
        
        Assert.assertEquals(result.toString(), "complete", 
            "Page should be fully loaded");
        
        // Test another JavaScript command
        String title = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return document.title;");
        
        Assert.assertFalse(title.isEmpty(), 
            "Should be able to execute JavaScript and get page title");
        
        logger.info("JavaScript execution smoke test passed");
    }
    
    @Test(priority = 5, groups = {"smoke", "ui"})
    public void smokeTestUIElements() {
        logger.info("Running smoke test for UI elements");
        
        googlePage.navigateToGoogle();
        
        // Test that we can interact with basic UI elements
        googlePage.enterSearchQuery("UI test");
        googlePage.clearSearchBox();
        
        // Verify search box is functional
        googlePage.enterSearchQuery("selenium webdriver");
        googlePage.clickSearchButton();
        
        // Verify results are displayed
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
            "UI interactions should work correctly");
        
        logger.info("UI elements smoke test passed");
    }
    
    @Test(priority = 6, groups = {"regression"})
    public void regressionTestSearchAccuracy() {
        logger.info("Running regression test for search accuracy");
        
        googlePage.navigateToGoogle();
        
        // Test specific search that should return predictable results
        String searchQuery = "Wikipedia";
        googlePage.search(searchQuery);
        
        // Get first few result titles
        var resultTitles = googlePage.getSearchResultTitles();
        Assert.assertFalse(resultTitles.isEmpty(), "Should have search results");
        
        // Verify at least one result is relevant (contains Wikipedia)
        boolean hasRelevantResult = resultTitles.stream()
            .anyMatch(title -> title.toLowerCase().contains("wikipedia"));
        
        Assert.assertTrue(hasRelevantResult, 
            "Search results should be relevant to query");
        
        logger.info("Search accuracy regression test passed");
    }
}