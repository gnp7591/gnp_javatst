# Allure Reports with TestNG Listeners - Implementation Guide

## Overview
You can now view Allure reports automatically after every test execution using a custom TestNG listener. The listener:
- ✓ Generates Allure reports from test results
- ✓ Automatically opens the report in your default browser
- ✓ Works on Windows, macOS, and Linux

## What Was Set Up

### 1. **AllureReportListener.java** (Created)
A custom TestNG `ISuiteListener` implementation that:
- Implements `onStart()` - Logs when test suite begins
- Implements `onFinish()` - Triggered after all tests complete
- Calls `generateAllureReport()` - Executes `allure generate` command
- Calls `openAllureReport()` - Opens the report in your browser

**Location:** `src/test/java/Base/AllureReportListener.java`

### 2. **testng.xml** (Updated)
Added the custom listener to your test configuration:
```xml
<listeners>
  <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
  <listener class-name="Base.AllureReportListener"/>
</listeners>
```

## Prerequisites

Make sure you have Allure CLI installed. If not, install it:

### Windows (PowerShell)
```powershell
scoop install allure
# or
choco install allure
```

### macOS
```bash
brew install allure
```

### Linux
```bash
sudo apt-get install allure
# or
sudo yum install allure
```

**Verify installation:**
```bash
allure --version
```

## How It Works

### Step 1: Run Your Tests
```bash
mvn clean test
# or for a specific test
mvn clean test -Dtest=LoginTest
```

### Step 2: Automatic Report Generation
After tests finish, the listener will:
1. Collect test results from `target/allure-results/`
2. Generate HTML report in `target/allure-report/`
3. Open report in your default browser

### Step 3: View Report
The Allure report will display:
- ✓ Test execution history
- ✓ Pass/Fail statistics
- ✓ Test duration
- ✓ Features and Stories (from @Feature and @Story annotations)
- ✓ Severity levels (from @Severity annotations)
- ✓ Screenshots and attachments
- ✓ Timeline view

## Current Configuration in Your Project

### Allure Annotations Already in Use
Your `LoginTest.java` already uses Allure annotations:
```java
@Feature("Login Functionality")
@Story("User Login Scenarios")
public class LoginTest extends UiBaseClass {

    @Test(description = "Verify that user can login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test verifies that a user can successfully log in with valid credentials.")
    public void testValidLogin() {
        // Test code...
    }
}
```

These annotations will automatically populate your Allure reports with:
- Features (Login Functionality)
- Stories (User Login Scenarios)
- Severity levels (BLOCKER, CRITICAL, etc.)
- Descriptions

## Advanced Usage - Adding Screenshots to Reports

To capture screenshots on test failure, add this to your test classes:

```java
import io.qameta.allure.Attachment;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UiBaseClass {
    
    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] captureScreenshot() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
    
    @AfterMethod
    public void afterTestMethod(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot();
        }
    }
}
```

## Command Line Options

### Run tests and auto-open report
```bash
mvn clean test
```

### Run specific test class
```bash
mvn clean test -Dtest=LoginTest
```

### Run specific test method
```bash
mvn clean test -Dtest=LoginTest#testValidLogin
```

### Manually generate report (without running tests)
```bash
allure generate target/allure-results -o target/allure-report --clean
```

### Manually open existing report
```bash
allure open target/allure-report
```

### View report with a different port
```bash
allure serve target/allure-results
```
(This starts a web server on http://localhost:4040)

## Troubleshooting

### Issue: Report doesn't auto-open
**Solution:** The listener tried but failed. Manually open:
- Windows: `start file:///C:/path/to/project/target/allure-report/index.html`
- macOS: `open file:///path/to/project/target/allure-report/index.html`
- Linux: `xdg-open file:///path/to/project/target/allure-report/index.html`

### Issue: "allure command not found"
**Solution:** Allure CLI is not installed. Install it using the prerequisites section above.

### Issue: Report shows no test results
**Solution:** Check that:
1. Tests actually ran (check `target/allure-results/` for JSON files)
2. `AllureTestNg` listener is properly configured in testng.xml
3. Clean the results before running: `mvn clean`

### Issue: Listener not triggered
**Solution:** Verify:
1. `AllureReportListener` class exists in `src/test/java/Base/`
2. `testng.xml` has the listener registered
3. Class name matches exactly: `Base.AllureReportListener`

## Customization

### Modify Report Generation Command
Edit `AllureReportListener.java` in the `generateAllureReport()` method:

```java
// Add custom history:
pb = new ProcessBuilder("cmd", "/c", "allure", "generate", ALLURE_RESULTS_DIR, 
                       "-o", ALLURE_REPORT_DIR, "--clean", 
                       "-h", "c:/path/to/history");
```

### Add Custom Report Directories
Modify the constants in `AllureReportListener.java`:
```java
private static final String ALLURE_RESULTS_DIR = "target/allure-results";
private static final String ALLURE_REPORT_DIR = "target/allure-report";
```

## Best Practices

1. ✓ Always run `mvn clean` before test execution
2. ✓ Use descriptive @Feature and @Story annotations
3. ✓ Set appropriate @Severity levels
4. ✓ Add screenshots on failure for UI tests
5. ✓ Use @Description for detailed test documentation
6. ✓ Organize tests by features/stories

## Summary

You now have:
- ✅ Automatic Allure report generation after tests
- ✅ Browser auto-open feature
- ✅ Cross-platform support (Windows, macOS, Linux)
- ✅ Integration with your existing TestNG suite
- ✅ All Allure annotations working in your tests

Just run `mvn clean test` and watch your Allure report open automatically!
