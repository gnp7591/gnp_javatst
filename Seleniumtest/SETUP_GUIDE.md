# All Items Test Automation - Complete Setup Guide

## 🎯 Project Overview

This is a comprehensive Selenium test automation framework for the Sauce Demo "All Items" page with:
- **28 Test Methods** in AllItemsTest.java covering all functionality
- **4 TestNG Suites** for different execution scenarios
- **Allure Reporting** integrated for detailed test reports
- **Page Object Model** implementation with InventoryPage.java
- **Parallel Execution** support for faster feedback

---

## 📋 Quick Start

### Easiest Way - Use the Runner Scripts

**Windows Batch (Command Prompt):**
```batch
run-tests.bat
```

**PowerShell:**
```powershell
.\run-tests.ps1
```

Both scripts provide an interactive menu to select which tests to run.

### Manual Maven Commands

```powershell
# Always use quotes around property values in PowerShell!

# Run smoke tests (quickest - 2-3 min)
mvn clean test -DsuiteXmlFile="testng-smoke.xml"

# Run all tests in grouped categories
mvn clean test -DsuiteXmlFile="testng-grouped.xml"

# Run all UiTests (auto-discovery)
mvn clean test -DsuiteXmlFile="testng-all-uitests.xml"

# Run default suite
mvn clean test
```

---

## 📁 Project Structure

```
Seleniumtest/
├── src/
│   ├── main/java/
│   │   └── test.java
│   └── test/java/
│       ├── Base/
│       │   ├── DriverFactory.java          # WebDriver management
│       │   ├── LoginPage.java              # Login page object
│       │   ├── InventoryPage.java          # All Items page object (enhanced)
│       │   ├── NavigationMenu.java         # Menu navigation
│       │   ├── ConfigReader.java           # Configuration management
│       │   ├── AllureReportListener.java   # Custom Allure listener
│       │   └── UiBaseClass.java            # Base class for tests
│       └── UiTests/
│           ├── LoginTest.java              # Login tests
│           ├── NavigationTest.java         # Navigation tests
│           └── AllItemsTest.java           # All Items tests (28 methods)
│
├── testng.xml                      # Main suite (organized by feature)
├── testng-smoke.xml               # Quick smoke tests
├── testng-grouped.xml             # Tests grouped by feature
├── testng-all-uitests.xml         # Auto-discovery all UiTests
├── pom.xml                        # Maven configuration
├── run-tests.bat                  # Windows Batch runner
├── run-tests.ps1                  # PowerShell runner
├── QUICK_START_SUITES.md          # Quick reference
├── TESTNG_SUITE_GUIDE.md          # Detailed guide
├── TEST_EXECUTION_SUMMARY.md      # Execution details & troubleshooting
└── README.md                      # This file
```

---

## 🧪 Test Coverage - AllItemsTest.java

### 28 Comprehensive Test Methods

**Page Load & Display (2 tests)**
- Verify page loads after login
- Verify products are displayed

**Product Information (5 tests)**
- Product count verification
- Product names display
- Product prices display & format validation
- Product descriptions display

**Sorting (4 tests)**
- Sort by price: low to high
- Sort by price: high to low
- Sort by name: A to Z
- Sort by name: Z to A

**Add to Cart (4 tests)**
- Add single product
- Add multiple products
- Add by product name
- Verify button text changes to REMOVE

**Remove from Cart (3 tests)**
- Remove single product
- Remove by product name
- Verify button text changes to ADD TO CART

**Cart Operations (3 tests)**
- Cart badge displays correct count
- Cart badge hidden when empty
- Cart badge displayed when items added

**Product Navigation (1 test)**
- Click product navigates to details page

---

## 📊 Available Test Suites

| Suite File | Tests | Duration | Use Case |
|------------|-------|----------|----------|
| **testng-smoke.xml** | 6 | 2-3 min | Quick CI/CD validation |
| **testng-grouped.xml** | 28 | 15-20 min | Feature-based testing |
| **testng-all-uitests.xml** | All UiTests | 20-25 min | Auto-discovery execution |
| **testng.xml** | All UiTests | 20-25 min | Main organized suite |

---

## 🔧 Key Files Explained

### InventoryPage.java (Enhanced Page Object)
```java
// Product retrieval
getAllProductNames()           // Get all product names
getAllProductPrices()          // Get all prices
getAllProductDescriptions()    // Get descriptions

// Cart operations
addProductToCart(index)        // Add by index
addProductToCartByName(name)   // Add by name
removeProductFromCart(index)   // Remove product

// Sorting
sortByPriceLowToHigh()         // Sort price ascending
sortByPriceHighToLow()         // Sort price descending
sortByNameAZ()                 // Sort name A-Z
sortByNameZA()                 // Sort name Z-A

// Verification
areProductsDisplayed()         // Check if products visible
isPricesSortedLowToHigh()      // Verify sort order
areNamesSortedAZ()             // Verify name sort
```

### pom.xml Configuration
- Maven Surefire plugin configured for TestNG
- Dynamic suite file selection via `${suiteXmlFile}` property
- Allure reporting integrated
- Aspect Weaver for Allure instrumentation

---

## 🚀 Running Tests

### Run Specific Test Class
```powershell
mvn clean test -Dtest=UiTests.AllItemsTest
```

### Run Specific Test Method
```powershell
mvn clean test -Dtest=UiTests.AllItemsTest#testAddProductToCart
```

### Run with Debug Output
```powershell
mvn clean test -X -DsuiteXmlFile="testng-smoke.xml"
```

### Skip Tests During Build
```powershell
mvn clean install -DskipTests
```

---

## 📈 Allure Reporting

### Generate Report
```powershell
mvn allure:report
```

### Generate and View Report (Opens Browser)
```powershell
mvn allure:serve
```

The report includes:
- Test execution timeline
- Pass/fail statistics
- Test details and duration
- Custom Allure annotations (Feature, Story, Severity, Description)
- Execution history

---

## ⚙️ Configuration

### Config Files Location
- **Test Configuration**: `src/test/resources/config.properties`
- **Allure Configuration**: `src/test/resources/allure.properties`

### Important Properties
```properties
# config.properties
app.url=https://www.saucedemo.com/
username.valid=standard_user
password.valid=secret_sauce
inventory.url=https://www.saucedemo.com/inventory.html
```

---

## 🐛 Troubleshooting

### Issue: "Unknown lifecycle phase .xml"
**Cause**: Missing quotes in PowerShell  
**Solution**: Always use quotes around property value
```powershell
✅ mvn clean test -DsuiteXmlFile="testng-smoke.xml"
❌ mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

### Issue: CDP Version Warnings
**Cause**: Chrome DevTools Protocol version mismatch  
**Solution**: Update pom.xml dependency to match your Chrome version
```xml
<!-- Check your Chrome version and update v### -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-devtools-v###</artifactId>
    <version>4.25.0</version>
</dependency>
```

### Issue: Tests Not Finding Elements
**Cause**: Stale element references or wrong locators  
**Solution**: Check if page objects have latest locators, or update locators for element changes

### Issue: Maven Not Found
**Cause**: Maven not installed or not in PATH  
**Solution**: 
1. Install Maven from https://maven.apache.org/download.cgi
2. Add Maven bin folder to system PATH

---

## 📝 Test Execution Best Practices

1. **Use Smoke Tests** for quick validation (2-3 minutes)
2. **Use Grouped Tests** for feature-specific testing
3. **Use Default Suite** for complete regression
4. **Check Allure Reports** after each run for detailed insights
5. **Run with `-X` flag** for debugging: `mvn clean test -X`

---

## 🔍 Test Data

### Default Test User
- **Username**: standard_user
- **Password**: secret_sauce
- **URL**: https://www.saucedemo.com/

### Available Products (6)
1. Sauce Labs Backpack - $29.99
2. Sauce Labs Bike Light - $9.99
3. Sauce Labs Bolt T-Shirt - $15.99
4. Sauce Labs Fleece Jacket - $49.99
5. Sauce Labs Onesie - $7.99
6. Test.allTheThings() T-Shirt (Red) - $15.99

---

## 📚 Documentation Files

- **QUICK_START_SUITES.md** - Quick reference for common commands
- **TESTNG_SUITE_GUIDE.md** - Detailed suite configuration guide
- **TEST_EXECUTION_SUMMARY.md** - Execution results & troubleshooting
- **ALLURE_SETUP_GUIDE.md** - Allure reporting setup

---

## ✅ Verification Checklist

- [ ] Maven is installed and working
- [ ] Java 21+ is installed
- [ ] Chrome browser is installed
- [ ] Test suites exist in project root
- [ ] pom.xml has dynamic suite property
- [ ] Configuration files are properly set
- [ ] Can run: `mvn clean test -DsuiteXmlFile="testng-smoke.xml"`
- [ ] Tests execute and Allure report generates

---

## 🎓 Next Steps

1. **Run Smoke Tests First**
   ```powershell
   mvn clean test -DsuiteXmlFile="testng-smoke.xml"
   ```

2. **View Test Report**
   ```powershell
   mvn allure:serve
   ```

3. **Analyze Results**
   - Check which tests passed/failed
   - Review test execution timeline
   - Examine detailed test logs

4. **Run Full Suite**
   ```powershell
   mvn clean test -DsuiteXmlFile="testng-grouped.xml"
   ```

---

## 📞 Support & Resources

### Useful Links
- [TestNG Documentation](https://testng.org/doc/)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Allure Reports](https://docs.qameta.io/allure/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Common Issues & Solutions
See **TEST_EXECUTION_SUMMARY.md** for detailed troubleshooting guide

---

## 📊 Test Metrics

- **Total Test Methods**: 28 (AllItems) + existing tests
- **Code Coverage**: Page Object Model with comprehensive assertions
- **Execution Time**: 2-25 minutes depending on suite
- **Parallel Execution**: 2-3 threads for faster feedback
- **Report Format**: Allure HTML reports with timelines

---

## 🏆 Key Features

✅ Page Object Model implementation  
✅ Comprehensive test coverage (28 methods)  
✅ Multiple suite configurations  
✅ Parallel execution support  
✅ Allure reporting integration  
✅ Custom listener implementation  
✅ Dynamic suite selection  
✅ Interactive runner scripts  
✅ Complete documentation  
✅ Troubleshooting guides  

---

**Last Updated**: May 31, 2026  
**Framework Version**: 1.0  
**Status**: ✅ Ready for Use

