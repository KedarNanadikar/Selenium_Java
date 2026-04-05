# Selenium Java Web Automation Framework 🚀

A comprehensive **Selenium WebDriver 4** automation framework built with **Java 11+**, **TestNG**, and **Maven** for robust web application testing.

## 📋 Table of Contents
- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Configuration](#️-configuration)
- [Running Tests](#-running-tests)
- [Browser Support](#-browser-support)
- [Reporting](#-reporting)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

## ✨ Features

### 🏗️ **Framework Architecture**
- **Page Object Model (POM)** - Maintainable and reusable page classes
- **WebDriverFactory** - Centralized browser management with ThreadLocal instances
- **BaseTest** - Common test setup and teardown functionality
- **Configuration Manager** - Centralized property-based configuration

### 🌐 **Cross-Browser Testing**
- **Chrome, Firefox, Edge** - Full support for major browsers
- **Headless Mode** - CI/CD friendly execution
- **Responsive Testing** - Multiple viewport sizes
- **Parallel Execution** - Run tests concurrently across browsers

### 📊 **Advanced Reporting**
- **ExtentReports** - Beautiful HTML reports with screenshots
- **Custom TestNG Listeners** - Enhanced console output and monitoring
- **Automatic Screenshots** - Captured on test failures
- **Test Execution Metrics** - Detailed timing and success rates

### 🛠️ **Testing Capabilities**
- **UI Testing** - Element interactions, form submissions, navigation
- **Smoke Tests** - Quick validation of core functionality
- **Regression Tests** - Comprehensive feature verification
- **Cross-Browser Tests** - Compatibility validation

## 🔧 Prerequisites

### Required Software
```bash
☑️ Java 11+ (JDK)
☑️ Maven 3.6+
☑️ Chrome Browser (latest)
☑️ Firefox Browser (latest)
☑️ Edge Browser (optional)
```

### Verify Installation
```bash
# Check Java version
java -version
javac -version

# Check Maven version
mvn -version

# Check browser versions
google-chrome --version
firefox --version
```

## 🚀 Quick Start

### 1. Clone & Setup
```bash
# Navigate to your workspace
cd "d:\\Kedar Company\\Repositories\\selenium_java"

# Verify project structure
ls -la
```

### 2. Install Dependencies
```bash
# Clean and install all dependencies
mvn clean install

# Skip tests during dependency installation
mvn clean install -DskipTests
```

### 3. Run Your First Test
```bash
# Run all tests (Chrome browser - default)
mvn test

# Run specific test class
mvn test -Dtest=GoogleSearchTest

# Run tests in Firefox
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dbrowser=headless
```

### 4. View Reports
```bash
# Open ExtentReports (after test execution)
start test-output/ExtentReports/ExtentReport_*.html

# View TestNG reports
start test-output/index.html
```

## 📁 Project Structure

```
selenium_java/
├── 📂 src/
│   ├── 📂 main/java/com/automation/
│   │   ├── 📂 base/
│   │   │   ├── 🔧 BaseTest.java           # Test foundation class
│   │   │   └── 📄 BasePage.java           # Common page methods
│   │   ├── 📂 config/
│   │   │   └── ⚙️  ConfigurationManager.java # Properties handler
│   │   ├── 📂 factory/
│   │   │   └── 🏭 WebDriverFactory.java   # Browser management
│   │   ├── 📂 listeners/
│   │   │   └── 📊 TestListener.java       # ExtentReports integration
│   │   └── 📂 pages/
│   │       └── 🔍 GoogleSearchPage.java   # Page Object example
│   └── 📂 test/java/com/automation/tests/
│       ├── 🧪 GoogleSearchTest.java       # Basic test scenarios
│       ├── 🌐 CrossBrowserTest.java       # Multi-browser tests
│       ├── 💨 SmokeTest.java             # Core functionality tests
│       └── 📂 listeners/
│           └── 👂 CustomTestListener.java  # Enhanced reporting
├── 📂 src/test/resources/
│   └── ⚙️  config.properties             # Test configuration
├── 📂 test-output/                       # Generated reports
├── 📋 testng.xml                         # TestNG suite configuration
├── 🔨 pom.xml                           # Maven dependencies
└── 📖 README.md                         # This file
```

## ⚙️ Configuration

### config.properties
```properties
# Browser Settings
browser=chrome
headless=false
implicit.wait=10
explicit.wait=30
page.load.timeout=30

# Application URLs
base.url=https://www.google.com
test.environment=QA

# Reporting
screenshot.on.failure=true
extent.reports.path=test-output/ExtentReports/
```

### Browser Profiles (Maven)
```xml
<!-- Chrome Profile -->
<profile>
    <id>chrome</id>
    <properties>
        <browser>chrome</browser>
    </properties>
</profile>

<!-- Firefox Profile -->
<profile>
    <id>firefox</id>
    <properties>
        <browser>firefox</browser>
    </properties>
</profile>

<!-- Headless Profile -->
<profile>
    <id>headless</id>
    <properties>
        <browser>headless</browser>
    </properties>
</profile>
```

## 🎯 Running Tests

### Command Line Options

#### Basic Execution
```bash
# Default browser (Chrome)
mvn test

# Specific browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=headless

# Specific test class
mvn test -Dtest=SmokeTest
mvn test -Dtest=GoogleSearchTest
mvn test -Dtest=CrossBrowserTest

# Specific test method
mvn test -Dtest=GoogleSearchTest#testBasicSearch
```

#### Advanced Execution
```bash
# Run by TestNG groups
mvn test -Dgroups="smoke"
mvn test -Dgroups="regression"
mvn test -Dgroups="smoke,regression"

# Parallel execution
mvn test -DparallelMode=methods -DthreadCount=3

# Maven profiles
mvn test -Pchrome
mvn test -Pfirefox
mvn test -Pheadless

# Custom properties
mvn test -Dbase.url="https://staging.google.com"
mvn test -Dimplicit.wait=15
```

#### CI/CD Friendly
```bash
# Production-ready command
mvn clean test -Dbrowser=headless -Dgroups="smoke,regression" -DparallelMode=methods -DthreadCount=2
```

### TestNG XML Execution
```bash
# Run specific XML suite
mvn test -DsuiteXmlFile=testng.xml

# Run with XML and browser override
mvn test -DsuiteXmlFile=testng.xml -Dbrowser=firefox
```

## 🌐 Browser Support

### Supported Browsers
| Browser | Versions | Headless | Mobile View |
|---------|----------|----------|-------------|
| Chrome  | 90+      | ✅       | ✅          |
| Firefox | 88+      | ✅       | ✅          |
| Edge    | 90+      | ✅       | ✅          |
| Safari  | 14+      | ❌       | ✅          |

### Browser Configuration
```java
// WebDriverFactory automatically manages:
✅ Driver downloads (WebDriverManager)
✅ Browser options and capabilities
✅ Window sizing and positioning
✅ Timeout configurations
✅ Headless mode settings
```

## 📊 Reporting

### ExtentReports Features
- **HTML Dashboard** - Interactive test results
- **Screenshots** - Auto-captured on failures
- **Test Timeline** - Execution chronology
- **System Information** - Environment details
- **Retry Mechanism** - Failed test re-execution

### Report Locations
```bash
📁 test-output/
├── 📊 ExtentReports/
│   ├── ExtentReport_2023-12-07_14-30-15.html
│   └── screenshots/
├── 📈 testng-results.xml
├── 📋 index.html
└── 📁 junitreports/
```

### Viewing Reports
```bash
# Windows
start test-output/ExtentReports/ExtentReport_*.html
start test-output/index.html

# macOS
open test-output/ExtentReports/ExtentReport_*.html

# Linux
xdg-open test-output/ExtentReports/ExtentReport_*.html
```

## 💡 Best Practices

### Page Object Model
```java
// ✅ Good - Using @FindBy annotations
@FindBy(id = "search-input")
private WebElement searchBox;

// ✅ Good - Action methods return page objects
public SearchResultsPage search(String query) {
    searchBox.sendKeys(query);
    searchButton.click();
    return new SearchResultsPage();
}
```

### Waiting Strategies
```java
// ✅ Explicit waits for dynamic content
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

// ✅ Fluent waits for complex conditions
Wait<WebDriver> fluentWait = new FluentWait<>(driver)
    .withTimeout(Duration.ofSeconds(30))
    .pollingEvery(Duration.ofSeconds(2))
    .ignoring(NoSuchElementException.class);
```

### Test Design
```java
// ✅ Independent tests
@Test
public void testUserLogin() {
    // Each test should be independent
    // Setup its own data and state
}

// ✅ Descriptive test names and assertions
@Test
public void shouldDisplayErrorMessageForInvalidCredentials() {
    Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
        "Error message should be displayed for invalid credentials");
}
```

## 🔍 Troubleshooting

### Common Issues & Solutions

#### 1. Browser Driver Issues
```bash
# Problem: Driver not found
❌ WebDriverException: chromedriver not found

# Solution: WebDriverManager handles this automatically
✅ Ensure internet connection for driver downloads
✅ Clear WebDriverManager cache: ~/.m2/repository/webdriver
```

#### 2. Element Not Found
```bash
# Problem: Element timing issues
❌ NoSuchElementException: Unable to locate element

# Solution: Implement proper waits
✅ Use WebDriverWait with ExpectedConditions
✅ Increase implicit wait timeout in config.properties
✅ Add debug screenshots to identify page state
```

#### 3. Browser Launch Failures
```bash
# Problem: Browser won't start
❌ SessionNotCreatedException: browser failed to start

# Solutions by browser:
✅ Chrome: Update to latest version, disable extensions
✅ Firefox: Check profile permissions, update browser
✅ Headless: Verify no GUI dependencies required
```

#### 4. Memory Issues
```bash
# Problem: OutOfMemoryError during execution
❌ java.lang.OutOfMemoryError: Java heap space

# Solutions:
✅ Increase heap size: mvn test -Xmx2048m
✅ Run tests sequentially: Remove parallel execution
✅ Ensure driver.quit() in @AfterMethod
```

### Debug Mode
```bash
# Enable debug logging
mvn test -Dlog4j.configuration=debug -X

# Run single test with detailed output
mvn test -Dtest=GoogleSearchTest -DforkCount=1 -DreuseForks=false
```

### Environment Verification
```bash
# Check current configuration
mvn test -Dtest=SmokeTest#smokeTestGoogleHomepage -Dbrowser=chrome

# Verify browser paths
where chrome
where firefox
where msedge
```

## 🤝 Contributing

### Adding New Tests
1. **Create test class** extending `BaseTest`
2. **Implement Page Objects** following POM pattern
3. **Add TestNG annotations** with appropriate groups
4. **Update testng.xml** if needed
5. **Add configuration** in config.properties

### Code Standards
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Implement proper exception handling
- Write independent, atomic tests

### Pull Request Process
1. Fork the repository
2. Create feature branch
3. Implement changes with tests
4. Ensure all tests pass
5. Submit pull request with description

---

## 📞 Support

### Getting Help
- **Documentation**: Check this README and inline JavaDoc
- **Issues**: Review test output and ExtentReports
- **Debugging**: Use IDE debugger with breakpoints
- **Community**: TestNG and Selenium documentation

### Key Resources
- [Selenium Documentation](https://selenium-python.readthedocs.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [ExtentReports](https://extentreports.com/)

---

**Happy Testing! 🎉**

*This framework provides a solid foundation for web automation testing. Customize it according to your specific application needs and testing requirements.*