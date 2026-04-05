package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleSearchPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for Google Search functionality
 */
public class GoogleSearchTest extends BaseTest {
    
    private GoogleSearchPage googlePage;
    
    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupPage() {
        googlePage = new GoogleSearchPage();
        googlePage.navigateToGoogle();
    }
    
    @Test(priority = 1)
    public void testGoogleHomepageLoad() {
        logger.info("Testing Google homepage load");
        
        // Verify page title
        String pageTitle = googlePage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Google"), "Page title should contain 'Google'");
        
        // Verify Google logo is displayed
        Assert.assertTrue(googlePage.isGoogleLogoDisplayed(), "Google logo should be displayed");
        
        logger.info("Google homepage loaded successfully");
    }
    
    @Test(priority = 2)
    public void testBasicSearch() {
        logger.info("Testing basic search functionality");
        
        String searchQuery = "Selenium WebDriver";
        googlePage.search(searchQuery);
        
        // Verify search results are displayed
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        
        // Verify we have search results
        int resultsCount = googlePage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0, "Should have at least one search result");
        
        // Verify search statistics are displayed
        String searchStats = googlePage.getSearchStats();
        Assert.assertFalse(searchStats.isEmpty(), "Search statistics should be displayed");
        
        logger.info("Basic search test completed successfully. Found {} results", resultsCount);
    }
    
    @Test(priority = 3)
    public void testSearchResultTitles() {
        logger.info("Testing search result titles");
        
        String searchQuery = "Java programming";
        googlePage.search(searchQuery);
        
        // Get search result titles
        var resultTitles = googlePage.getSearchResultTitles();
        
        // Verify we have result titles
        Assert.assertFalse(resultTitles.isEmpty(), "Should have search result titles");
        
        // Verify at least one title contains our search term
        boolean containsSearchTerm = resultTitles.stream()
            .anyMatch(title -> title.toLowerCase().contains("java"));
        Assert.assertTrue(containsSearchTerm, "At least one result title should contain 'java'");
        
        logger.info("Search result titles test completed. Found {} titles", resultTitles.size());
    }
    
    @Test(priority = 4)
    public void testMultipleSearches() {
        logger.info("Testing multiple consecutive searches");
        
        String[] searchQueries = {"TestNG framework", "Maven build tool", "Selenium Grid"};
        
        for (String query : searchQueries) {
            // Clear previous search and enter new query
            googlePage.clearSearchBox();
            googlePage.search(query);
            
            // Verify results for each search
            Assert.assertTrue(googlePage.areSearchResultsDisplayed(), 
                "Search results should be displayed for query: " + query);
            
            int resultsCount = googlePage.getSearchResultsCount();
            Assert.assertTrue(resultsCount > 0, 
                "Should have results for query: " + query);
            
            logger.info("Search for '{}' returned {} results", query, resultsCount);
        }
        
        logger.info("Multiple searches test completed successfully");
    }
    
    @Test(priority = 5)
    public void testSearchBoxFunctionality() {
        logger.info("Testing search box functionality");
        
        // Test entering query without clicking search
        String testQuery = "Automation Testing";
        googlePage.enterSearchQuery(testQuery);
        
        // Verify query is entered (we can't directly get input value, so we'll clear and re-enter)
        googlePage.clearSearchBox();
        googlePage.enterSearchQuery(testQuery);
        
        // Now click search button separately
        googlePage.clickSearchButton();
        
        // Verify results
        Assert.assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        
        logger.info("Search box functionality test completed successfully");
    }
    
    @Test(priority = 6, enabled = false) // Disabled as "I'm Feeling Lucky" redirects immediately
    public void testFeelingLuckyButton() {
        logger.info("Testing 'I'm Feeling Lucky' button");
        
        googlePage.enterSearchQuery("Selenium official website");
        googlePage.clickFeelingLucky();
        
        // Note: This will redirect to the first search result directly,
        // so we need to verify we're no longer on Google search page
        String currentUrl = googlePage.getCurrentUrl();
        Assert.assertFalse(currentUrl.contains("google.com/search"), 
            "Should have navigated away from Google search");
        
        logger.info("'I'm Feeling Lucky' button test completed");
    }
}