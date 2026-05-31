# Quick Start - Running TestNG Suites

## Problem Fixed ✅

The pom.xml has been updated to support dynamic suite file selection via Maven property.

---

## How to Run Each Suite

### 1. Default Suite (testng.xml)
Organized tests by feature (Login, Navigation, All Items)
```bash
mvn clean test
```

### 2. All UiTests Suite (testng-all-uitests.xml)
Automatically discovers all tests in UiTests package
```powershell
# Windows PowerShell - Use quotes around property value
mvn clean test -DsuiteXmlFile="testng-all-uitests.xml"
```

### 3. Grouped Suite (testng-grouped.xml)
Features grouped into: Critical, Product Display, Cart Operations
```powershell
# Windows PowerShell - Use quotes around property value
mvn clean test -DsuiteXmlFile="testng-grouped.xml"
```

### 4. Smoke Tests (testng-smoke.xml)
Quick validation - 6 essential tests (~2-3 minutes)
```powershell
# Windows PowerShell - Use quotes around property value
mvn clean test -DsuiteXmlFile="testng-smoke.xml"
```

---

## Common Commands

```powershell
# Run specific test class
mvn clean test -Dtest=UiTests.AllItemsTest

# Run specific test method
mvn clean test -Dtest=UiTests.AllItemsTest#testAddProductToCart

# Clean and run smoke tests with debug output
mvn clean test -X -DsuiteXmlFile="testng-smoke.xml"

# Skip tests during build
mvn clean install -DskipTests

# Important: Always use quotes around suite file property value in PowerShell!
# ✅ CORRECT:   mvn clean test -DsuiteXmlFile="testng-smoke.xml"
# ❌ WRONG:     mvn clean test -DsuiteXmlFile=testng-smoke.xml
```

---

## Generate Allure Report

After test execution:

```bash
# Generate the report
mvn allure:report

# Generate and serve the report (opens browser)
mvn allure:serve
```

---

## Verify Suite Files Location

All suite files should be in the project root:
```
Seleniumtest/
├── testng.xml
├── testng-all-uitests.xml
├── testng-grouped.xml
├── testng-smoke.xml
└── pom.xml
```

---

## What Changed in pom.xml

1. **Added Property**: `<suiteXmlFile>testng.xml</suiteXmlFile>`
   - Default value: testng.xml
   - Can be overridden via: `-DsuiteXmlFile=<filename>`

2. **Updated Surefire Config**: 
   - Changed: `<suiteXmlFile>testng.xml</suiteXmlFile>`
   - To: `<suiteXmlFile>${suiteXmlFile}</suiteXmlFile>`
   - Now uses the property value instead of hardcoded filename

---

## Troubleshooting

### Suite still not running?
1. Ensure you're in the correct directory: `C:\Users\gnpur\git\gnp_javatst\Seleniumtest`
2. Run: `mvn clean test -DsuiteXmlFile=testng-smoke.xml` (start with smoke tests)
3. Check for errors in the console output

### Tests compile but don't execute?
1. Verify suite files exist in project root
2. Check file names are exactly: `testng*.xml`
3. Run: `mvn clean test -X` to see debug output

### Module/Dependency issues?
```bash
# Clean Maven cache and rebuild
mvn clean install -DskipTests

# Then run tests
mvn test -DsuiteXmlFile=testng-smoke.xml
```

---

## Test Execution Times

| Suite | Tests | Time | Use |
|-------|-------|------|-----|
| testng-smoke.xml | 6 | 2-3 min | Quick CI/CD check |
| testng-grouped.xml | 28 | 15-20 min | Feature testing |
| testng-all-uitests.xml | All UiTests | 20-25 min | Full coverage |
| testng.xml | All UiTests | 20-25 min | Main suite |

---

## Next Steps

✅ Updated pom.xml with dynamic suite property
✅ All 4 suite files configured and ready
✅ Allure reporting configured
✅ Test classes in UiTests package ready to execute

Now run: `mvn clean test -DsuiteXmlFile=testng-smoke.xml`
