package com.automation.listeners;

import com.automation.config.ConfigurationManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG Listener for ExtentReports integration
 */
public class TestListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private ConfigurationManager config;
    
    public TestListener() {
        config = ConfigurationManager.getInstance();
    }
    
    /**
     * Initialize ExtentReports
     */
    public synchronized void initializeExtentReports() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportsPath = config.getExtentReportsPath();
            String reportName = reportsPath + "ExtentReport_" + timestamp + ".html";
            
            // Create directory if it doesn't exist
            File reportsDir = new File(reportsPath);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportName);
            
            // Configure report
            sparkReporter.config().setDocumentTitle("Selenium Automation Test Report");
            sparkReporter.config().setReportName("Web Automation Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // Set system information
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", config.getBrowser());
            extent.setSystemInfo("Environment", config.getTestEnvironment());
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        if (extent == null) {
            initializeExtentReports();
        }
        
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        
        ExtentTest test = extent.createTest(testName)
                .assignCategory(className)
                .assignAuthor("Automation Team");
        
        extentTest.set(test);
        
        test.log(Status.INFO, "Test started: " + testName);
        test.log(Status.INFO, "Test class: " + className);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, 
                MarkupHelper.createLabel("Test PASSED: " + result.getMethod().getMethodName(), 
                ExtentColor.GREEN));
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, 
                MarkupHelper.createLabel("Test FAILED: " + result.getMethod().getMethodName(), 
                ExtentColor.RED));
            
            // Log failure reason
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.log(Status.FAIL, "Failure Reason: " + throwable.getMessage());
                test.log(Status.FAIL, throwable);
            }
            
            // Capture screenshot on failure
            if (config.isScreenshotOnFailure()) {
                captureScreenshot(result.getMethod().getMethodName());
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP, 
                MarkupHelper.createLabel("Test SKIPPED: " + result.getMethod().getMethodName(), 
                ExtentColor.ORANGE));
            
            // Log skip reason
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.log(Status.SKIP, "Skip Reason: " + throwable.getMessage());
            }
        }
    }
    
    @Override
    public void onFinish(org.testng.ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
    
    /**
     * Capture screenshot and attach to ExtentReports
     * @param testName name of the test
     */
    private void captureScreenshot(String testName) {
        try {
            // Get WebDriver instance using reflection or ThreadLocal
            WebDriver driver = getDriverInstance();
            
            if (driver != null && driver instanceof TakesScreenshot) {
                TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
                File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
                
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                String screenshotPath = config.getExtentReportsPath() + "screenshots/" + 
                                      testName + "_" + timestamp + ".png";
                
                File screenshotDir = new File(config.getExtentReportsPath() + "screenshots/");
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }
                
                File destFile = new File(screenshotPath);
                FileUtils.copyFile(sourceFile, destFile);
                
                ExtentTest test = extentTest.get();
                if (test != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
                }
            }
        } catch (Exception e) {
            ExtentTest test = extentTest.get();
            if (test != null) {
                test.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get WebDriver instance from ThreadLocal or other source
     * This method should be implemented based on how WebDriver is managed in your framework
     * @return WebDriver instance
     */
    private WebDriver getDriverInstance() {
        try {
            // Try to get driver using reflection from WebDriverFactory
            Class<?> factoryClass = Class.forName("com.automation.factory.WebDriverFactory");
            java.lang.reflect.Method getDriverMethod = factoryClass.getMethod("getDriver");
            return (WebDriver) getDriverMethod.invoke(null);
        } catch (Exception e) {
            // If reflection fails, return null
            return null;
        }
    }
    
    /**
     * Get current ExtentTest instance
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Log info message to ExtentReports
     * @param message info message
     */
    public static void logInfo(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log pass message to ExtentReports
     * @param message pass message
     */
    public static void logPass(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }
    
    /**
     * Log fail message to ExtentReports
     * @param message fail message
     */
    public static void logFail(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
}