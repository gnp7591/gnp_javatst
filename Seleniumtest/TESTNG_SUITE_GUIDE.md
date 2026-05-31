# TestNG Suite Configuration Guide

This document describes the different TestNG suite configurations available for running UI tests.

## Available Test Suite Files

### 1. **testng.xml** (Primary - Organized by Feature)
- **Purpose**: Main test suite with organized test grouping
- **Organization**: Tests grouped into logical categories
  - Login Tests
  - Navigation Tests
  - All Items Tests (organized by functionality)
- **Thread Count**: 3 parallel threads
- **Use Case**: Comprehensive testing with organized output

**Run Command:**
```bash
mvn clean test
```

---

### 2. **testng-all-uitests.xml** (Quick & Simple)
- **Purpose**: Runs all tests from UiTests package
- **Organization**: Package-based scanning (automatic discovery)
- **Thread Count**: 3 parallel threads
- **Features**:
  - Automatically discovers all test classes in UiTests package
  - No need to manually add new test classes
  - Tests run in parallel

**Run Command:**
```bash
mvn clean test -DsuiteXmlFile=testng-all-uitests.xml
```

---

### 3. **testng-grouped.xml** (Feature-Based Execution)
- **Purpose**: Group tests by feature for selective execution
- **Test Groups**:
  1. **Critical Tests** (1 thread) - Login + Navigation
  2. **Product Display and Sorting** (2 threads) - Product info and sorting tests
  3. **Cart Operations** (2 threads) - Cart functionality tests
- **Use Case**: Run specific feature tests or skip certain categories

**Run Command:**
```bash
mvn clean test -DsuiteXmlFile=testng-grouped.xml
```

---

### 4. **testng-smoke.xml** (Quick Smoke Tests)
- **Purpose**: Quick smoke tests to verify basic functionality
- **Execution**: Sequential (no parallel) for stability
- **Tests Included**:
  - Login functionality
  - Navigation
  - All Items page load
  - Basic cart operations
- **Use Case**: Quick CI/CD checks before full regression
- **Execution Time**: ~2-3 minutes

**Run Command:**
```bash
mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

---

## Test Classes Included

### UiTests Package
1. **LoginTest.java** - Login functionality and authentication
2. **NavigationTest.java** - Navigation menu and page transitions
3. **AllItemsTest.java** - Comprehensive All Items page tests (28 test methods)

### AllItemsTest.java - Test Categories (28 Tests)

#### Page Load and Display (2 tests)
- testAllItemsPageLoads
- testAllItemsDisplay

#### Product Information (4 tests)
- testProductCount
- testProductNamesDisplay
- testProductPricesDisplay
- testProductPricesFormatValid
- testProductDescriptionsDisplay

#### Sorting Functionality (4 tests)
- testSortByPriceLowToHigh
- testSortByPriceHighToLow
- testSortByNameAZ
- testSortByNameZA

#### Add to Cart (4 tests)
- testAddProductToCart
- testAddMultipleProductsToCart
- testAddProductToCartByName
- testButtonTextChangesAfterAddToCart

#### Remove from Cart (3 tests)
- testRemoveProductFromCart
- testRemoveProductFromCartByName
- testButtonTextChangesAfterRemoveFromCart

#### Cart Badge (3 tests)
- testCartBadgeDisplaysCorrectCount
- testCartBadgeHiddenWhenEmpty
- testCartBadgeDisplayedWhenItemsAdded

#### Product Navigation (1 test)
- testClickProductNavigates

---

## How to Run Tests

### Run Main Suite
```bash
mvn clean test
```

### Run Specific Suite (Windows PowerShell)
```powershell
# Use quotes around the property value for proper parsing
mvn clean test -DsuiteXmlFile="<suite-filename>"
```

### Examples:
```powershell
# Run all UiTests (auto-discovery)
mvn clean test -DsuiteXmlFile="testng-all-uitests.xml"

# Run grouped tests
mvn clean test -DsuiteXmlFile="testng-grouped.xml"

# Run smoke tests (fastest - 2-3 min)
mvn clean test -DsuiteXmlFile="testng-smoke.xml"

# Run specific test class
mvn clean test -Dtest=UiTests.AllItemsTest

# Run specific test method
mvn clean test -Dtest=UiTests.AllItemsTest#testAddProductToCart
```

### Important Note for Windows PowerShell Users
Always use **quotes** around the Maven property value:
- ✅ **CORRECT**: `mvn clean test -DsuiteXmlFile="testng-smoke.xml"`
- ❌ **WRONG**: `mvn clean test -DsuiteXmlFile=testng-smoke.xml` (causes parsing error)

---

## Test Execution Scenarios

### Quick Verification (5 minutes)
Use `testng-smoke.xml` - Tests critical functionality quickly

### Feature Testing (15-20 minutes)
Use `testng-grouped.xml` - Run specific feature groups

### Full Regression (25-30 minutes)
Use `testng.xml` or `testng-all-uitests.xml` - Complete test coverage

### CI/CD Pipeline
Use `testng-smoke.xml` for quick feedback, then `testng-all-uitests.xml` for full validation

---

## Listeners Configuration

All suites are configured with:
- **AllureTestNg** - Allure report generation
- **AllureReportListener** - Custom Allure reporting for additional details

### Generate Allure Report
After test execution:
```bash
mvn allure:report
```

Open report:
```bash
mvn allure:serve
```

---

## Parallel Execution Details

### Thread Count Configuration
- **testng.xml**: 3 parallel test threads
- **testng-all-uitests.xml**: 3 parallel test threads
- **testng-grouped.xml**: 
  - Critical Tests: 1 thread (sequential)
  - Product & Sorting: 2 threads
  - Cart Operations: 2 threads
- **testng-smoke.xml**: 1 thread (sequential for stability)

### Best Practices
- Use parallel execution for faster feedback in CI/CD
- Use sequential execution for debugging issues
- Adjust thread count based on environment capabilities

---

## Configuration Modification

### Add New Test Class
1. Create test class in `src/test/java/UiTests/`
2. For `testng.xml`: Manually add `<class>` element
3. For `testng-all-uitests.xml`: Automatic (package-based discovery)
4. For `testng-grouped.xml`: Add under appropriate test section

### Add New Test Method
- Automatically picked up by all suite configurations
- Ensure it has `@Test` annotation
- Add to `testng.xml` methods section if selective execution needed

---

## Troubleshooting

### Tests Not Running
- Verify test classes are in `src/test/java/UiTests/` package
- Check suite file is referenced correctly: `mvn clean test -DsuiteXmlFile=testng-<name>.xml`
- Ensure suite XML files are in project root directory
- Ensure `@Test` annotation is present on test methods
- Run `mvn clean test` first to verify pom.xml is correctly configured

### Parallel Execution Issues
- If tests fail in parallel, try sequential: use `testng-smoke.xml` or reduce thread count
- Check for shared state or resource conflicts
- Ensure `@BeforeMethod` and `@AfterMethod` are thread-safe

### Report Generation Issues
- Clear `target/allure-results/` before running tests
- Ensure Allure plugin is configured in `pom.xml`
- Check Java/Maven versions compatibility

---

## Maintenance

### Review Test Organization
- Periodically review test grouping in `testng-grouped.xml`
- Add new high-value tests to `testng-smoke.xml`
- Keep test descriptions updated

### Monitor Test Execution
- Track test execution times
- Identify flaky tests
- Optimize thread count based on results

---

