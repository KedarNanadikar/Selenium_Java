package com.automation.pages;

import com.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Google Search page object implementing Page Object Model pattern
 */
public class GoogleSearchPage extends BasePage {
    
    // Page elements using @FindBy annotations
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(xpath = "//input[@name='btnK']")
    private WebElement searchButton;
    
    @FindBy(xpath = "//input[@name='btnI']")
    private WebElement feelingLuckyButton;
    
    @FindBy(xpath = "//div[@id='search']//h3")
    private List<WebElement> searchResults;
    
    @FindBy(xpath = "//div[@id='result-stats']")
    private WebElement resultStats;
    
    @FindBy(xpath = "//button[contains(text(), 'Accept all') or contains(text(), 'I agree') or contains(text(), 'Accept')]")
    private WebElement acceptCookiesButton;
    
    @FindBy(xpath = "//img[@alt='Google']")
    private WebElement googleLogo;
    
    /**
     * Navigate to Google homepage
     */
    public void navigateToGoogle() {
        navigateTo("https://www.google.com");
        logger.info("Navigated to Google homepage");
        
        // Handle cookies popup if present
        handleCookiesPopup();
    }
    
    /**
     * Handle cookies popup if present
     */
    public void handleCookiesPopup() {
        try {
            // Wait a moment for popup to appear
            Thread.sleep(1000);
            if (isDisplayed(acceptCookiesButton)) {
                click(acceptCookiesButton);
                logger.info("Accepted cookies");
            }
        } catch (Exception e) {
            logger.debug("No cookies popup found or already handled");
        }
    }
    
    /**
     * Perform search with given query
     * @param query Search query
     */
    public void search(String query) {
        type(searchBox, query);
        click(searchButton);
        logger.info("Searched for: {}", query);
    }
    
    /**
     * Enter search query without clicking search button
     * @param query Search query
     */
    public void enterSearchQuery(String query) {
        type(searchBox, query);
        logger.info("Entered search query: {}", query);
    }
    
    /**
     * Click search button
     */
    public void clickSearchButton() {
        click(searchButton);
        logger.info("Clicked search button");
    }
    
    /**
     * Click "I'm Feeling Lucky" button
     */
    public void clickFeelingLucky() {
        click(feelingLuckyButton);
        logger.info("Clicked 'I'm Feeling Lucky' button");
    }
    
    /**
     * Get search results count
     * @return Number of search results
     */
    public int getSearchResultsCount() {
        try {
            // Wait for search results to load
            waitForElementPresent(By.xpath("//div[@id='search']//h3"));
            int count = searchResults.size();
            logger.info("Found {} search results", count);
            return count;
        } catch (Exception e) {
            logger.warn("Could not get search results count", e);
            return 0;
        }
    }
    
    /**
     * Get search result titles
     * @return List of search result titles
     */
    public List<String> getSearchResultTitles() {
        return searchResults.stream()
            .map(this::getText)
            .filter(title -> !title.isEmpty())
            .toList();
    }
    
    /**
     * Click on first search result
     */
    public void clickFirstSearchResult() {
        if (!searchResults.isEmpty()) {
            click(searchResults.get(0));
            logger.info("Clicked on first search result");
        } else {
            logger.warn("No search results found to click");
        }
    }
    
    /**
     * Get search statistics text
     * @return Search stats text
     */
    public String getSearchStats() {
        try {
            waitForElementVisible(resultStats);
            return getText(resultStats);
        } catch (Exception e) {
            logger.debug("Could not get search stats", e);
            return "";
        }
    }
    
    /**
     * Check if search results are displayed
     * @return true if results are displayed, false otherwise
     */
    public boolean areSearchResultsDisplayed() {
        try {
            return !searchResults.isEmpty() && isDisplayed(searchResults.get(0));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Clear search box
     */
    public void clearSearchBox() {
        searchBox.clear();
        logger.info("Cleared search box");
    }
    
    /**
     * Get placeholder text from search box
     * @return Placeholder text
     */
    public String getSearchBoxPlaceholder() {
        return searchBox.getAttribute("placeholder");
    }
    
    /**
     * Check if Google logo is displayed
     * @return true if logo is displayed, false otherwise
     */
    public boolean isGoogleLogoDisplayed() {
        try {
            return isDisplayed(googleLogo);
        } catch (Exception e) {
            logger.debug("Google logo not found", e);
            return false;
        }
    }
    
    /**
     * Get current search query from search box
     * @return Current search query
     */
    public String getCurrentSearchQuery() {
        return searchBox.getAttribute("value");
    }
    
    /**
     * Check if search button is enabled
     * @return true if search button is enabled
     */
    public boolean isSearchButtonEnabled() {
        return isEnabled(searchButton);
    }
    
    /**
     * Get page title
     * @return page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     * @return current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}