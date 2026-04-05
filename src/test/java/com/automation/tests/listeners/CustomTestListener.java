package com.automation.tests.listeners;

import com.automation.config.ConfigurationManager;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Custom TestNG listener for enhanced test execution reporting and monitoring
 */
public class CustomTestListener implements ITestListener, ISuiteListener, IReporter {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    
    @Override
    public void onStart(ISuite suite) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 STARTING TEST SUITE: " + suite.getName());
        System.out.println("📅 Start Time: " + formatter.format(new Date()));
        System.out.println("🌐 Browser: " + getBrowserFromSuite(suite));
        System.out.println("=".repeat(80) + "\n");
    }
    
    @Override
    public void onFinish(ISuite suite) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✅ FINISHED TEST SUITE: " + suite.getName());
        System.out.println("📅 End Time: " + formatter.format(new Date()));
        System.out.println("=".repeat(80) + "\n");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶️  Starting: " + getTestName(result) + 
            " [" + formatter.format(new Date()) + "]");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("✅ PASSED: " + getTestName(result) + 
            " (" + duration + "ms)");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("❌ FAILED: " + getTestName(result) + 
            " (" + duration + "ms)");
        
        // Print failure reason
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            System.out.println("   💥 Reason: " + throwable.getMessage());
            
            // Print first few stack trace elements for context
            StackTraceElement[] elements = throwable.getStackTrace();
            for (int i = 0; i < Math.min(3, elements.length); i++) {
                System.out.println("      at " + elements[i]);
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️  SKIPPED: " + getTestName(result));
        
        // Print skip reason if available
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            System.out.println("   ℹ️  Reason: " + throwable.getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("⚠️  FAILED (within success %): " + getTestName(result));
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n📋 Test Context: " + context.getName());
        System.out.println("   📝 Total Tests: " + context.getAllTestMethods().length);
        System.out.println("   🏷️  Groups: " + String.join(", ", context.getIncludedGroups()));
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n📊 Test Context Summary for: " + context.getName());
        System.out.println("   ✅ Passed: " + context.getPassedTests().size());
        System.out.println("   ❌ Failed: " + context.getFailedTests().size());
        System.out.println("   ⏭️  Skipped: " + context.getSkippedTests().size());
        System.out.println("   ⏱️  Duration: " + 
            (context.getEndDate().getTime() - context.getStartDate().getTime()) + "ms\n");
    }
    
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        // Generate custom summary report
        System.out.println("\n" + "=".repeat(100));
        System.out.println("📈 FINAL TEST EXECUTION SUMMARY");
        System.out.println("=".repeat(100));
        
        int totalTests = 0;
        int totalPassed = 0;
        int totalFailed = 0;
        int totalSkipped = 0;
        
        for (ISuite suite : suites) {
            System.out.println("\n🔍 Suite: " + suite.getName());
            
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext context = suiteResult.getTestContext();
                
                int passed = context.getPassedTests().size();
                int failed = context.getFailedTests().size();
                int skipped = context.getSkippedTests().size();
                int total = passed + failed + skipped;
                
                totalTests += total;
                totalPassed += passed;
                totalFailed += failed;
                totalSkipped += skipped;
                
                System.out.println("   📋 Context: " + context.getName());
                System.out.println("      Total: " + total + " | ✅ " + passed + 
                    " | ❌ " + failed + " | ⏭️ " + skipped);
                
                if (failed > 0) {
                    System.out.println("      🚨 Failed Tests:");
                    context.getFailedTests().getAllResults().forEach(result -> {
                        System.out.println("         - " + getTestName(result));
                    });
                }
            }
        }
        
        System.out.println("\n📊 OVERALL RESULTS:");
        System.out.println("   🔢 Total Tests: " + totalTests);
        System.out.println("   ✅ Passed: " + totalPassed + " (" + 
            (totalTests > 0 ? (totalPassed * 100 / totalTests) : 0) + "%)");
        System.out.println("   ❌ Failed: " + totalFailed + " (" + 
            (totalTests > 0 ? (totalFailed * 100 / totalTests) : 0) + "%)");
        System.out.println("   ⏭️  Skipped: " + totalSkipped + " (" + 
            (totalTests > 0 ? (totalSkipped * 100 / totalTests) : 0) + "%)");
        
        // Overall status
        if (totalFailed == 0) {
            System.out.println("\n🎉 ALL TESTS PASSED! 🎉");
        } else {
            System.out.println("\n⚠️  SOME TESTS FAILED - Review results above");
        }
        
        System.out.println("=".repeat(100) + "\n");
    }
    
    private String getTestName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
    
    private String getBrowserFromSuite(ISuite suite) {
        // Try to get browser from suite parameters
        String browser = suite.getParameter("browser");
        if (browser != null) {
            return browser;
        }
        
        // Fallback to configuration
        try {
            return ConfigurationManager.getInstance().getBrowser();
        } catch (Exception e) {
            return "default";
        }
    }
}