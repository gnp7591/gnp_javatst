# Selenium Test Automation Project

A comprehensive test automation framework built with **Selenium WebDriver**, **TestNG**, and **Maven** for automating UI and API testing.

## Project Overview

This project provides a robust testing framework with:
- **UI Testing**: Automated browser testing using Selenium WebDriver
- **API Testing**: REST API testing using REST Assured
- **BDD Support**: Behavior-driven development with Cucumber
- **Test Management**: TestNG test framework for organizing and executing tests
- **Cross-browser Support**: Chrome 148+ with automatic driver management

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── test.java          # Main application source
│   └── resources/             # Application resources
└── test/
    ├── java/
    │   ├── apiTests/          # API test classes
    │   ├── Base/              # Base test classes and utilities
    │   │   ├── DriverFactory.java      # WebDriver factory
    │   │   ├── LoginPage.java          # Page object model
    │   │   └── UiBaseClass.java        # Base class for UI tests
    │   ├── PracticePgms/      # Practice/sample programs
    │   ├── testdemo/          # Demo test classes
    │   └── UiTests/           # UI test cases
    └── resources/             # Test resources and configurations
```

## Key Dependencies

- **Selenium WebDriver** (4.25.0) - Browser automation
- **Selenium DevTools** (v128) - Chrome DevTools Protocol support
- **TestNG** (7.10.1) - Test framework
- **WebDriverManager** (6.1.0) - Automatic driver management
- **REST Assured** (5.4.0) - API testing
- **Cucumber** (7.15.0) - BDD framework
- **Jackson** (2.17.0-rc1) - JSON processing

## Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.8.9 or later
- **Chrome Browser**: Version 148+ (for WebDriver testing)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Seleniumtest
```

### 2. Install Dependencies
```bash
mvn clean install
```

This command will:
- Download all Maven dependencies
- Compile the source code
- Run tests (if configured)
- Create the JAR file in the `target/` directory

### 3. Verify Installation
```bash
mvn --version
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run Tests with TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run with Maven Surefire Plugin (Specific suite)
```bash
mvn test -Dtest=UiTests.*
```

## Test Structure

### UI Tests
Located in `src/test/java/UiTests/`

**Example - LoginTest.java:**
```java
@Test(description = "Verify that user can login with valid credentials")
public void testValidLogin() {
    DriverFactory.getDriver().get("https://www.saucedemo.com/");
    LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    loginPage.login("standard_user", "secret_sauce");
    Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), 
                       "https://www.saucedemo.com/inventory.html");
}
```

### Base Classes

**DriverFactory.java**: Manages WebDriver instances
```java
DriverFactory.getDriver();  // Get WebDriver instance
```

**LoginPage.java**: Page Object Model for login page
```java
LoginPage loginPage = new LoginPage(driver);
loginPage.login(username, password);
```

**UiBaseClass.java**: Base class providing common setup/teardown

### API Tests
Located in `src/test/java/apiTests/`

Includes REST API testing using REST Assured framework.

## Test Configuration

### TestNG Configuration
Edit `testng.xml` to configure:
- Test class locations
- Test method grouping
- Parallel execution
- Test parameters

Example:
```xml
<suite name="Default Suite">
    <test name="Default Test">
        <classes>
            <class name="UiTests.LoginTest" />
        </classes>
    </test>
</suite>
```

## WebDriver Management

The project uses **WebDriverManager** for automatic driver management:
- Automatically downloads the correct ChromeDriver version
- No manual driver setup required
- Supports Chrome 148+

## Test Reports

After running tests, reports are generated in:
- **TestNG Reports**: `test-output/` directory
- **HTML Report**: `test-output/index.html`
- **JUnit Reports**: `test-output/junitreports/`

Open `test-output/index.html` in a browser to view the test execution report.

## Troubleshooting

### CDP Version Mismatch
If you see warnings about Chrome DevTools Protocol:
```
WARNING: Unable to find version of CDP to use for 148.0.7778.179
```

This is informational and resolved by the installed `selenium-devtools-v128` dependency.

### Maven Not Found
```bash
mvn -v
```

If Maven is not installed:
1. Download from https://maven.apache.org/
2. Extract and add `bin/` directory to PATH
3. Verify with `mvn -v`

### ChromeDriver Issues
WebDriverManager handles driver management automatically. If issues persist:
```bash
mvn clean install -U
```

## Best Practices

1. **Page Object Model**: Use page classes (LoginPage.java) to encapsulate page interactions
2. **Base Classes**: Extend UiBaseClass for common test setup/teardown
3. **Assertions**: Use TestNG assertions for test validations
4. **Data Management**: Use TestNG `@DataProvider` for test data
5. **Logging**: Add logs for debugging test failures

## IDE Setup

### IntelliJ IDEA
1. Open project folder
2. Maven dependencies will auto-download
3. Right-click on test class → Run 'ClassName'

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select project root directory
3. Right-click on test → Run As → TestNG Test

### VS Code
1. Install "Maven for Java" extension
2. Install "Test Runner for Java" extension
3. Test methods will show "Run" links above them

## Contributing

1. Create a new branch for features
2. Write tests for new functionality
3. Ensure all tests pass: `mvn clean test`
4. Commit and push changes
5. Create a pull request

## Maintenance

Keep dependencies updated:
```bash
mvn versions:display-dependency-updates
mvn clean install
```

## Additional Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [REST Assured](https://rest-assured.io/)
- [Cucumber](https://cucumber.io/)

## License

[Add your license information here]

## Contact

For questions or support, please reach out to the development team.

---

**Last Updated**: May 30, 2026  
**Project Version**: 0.0.1-SNAPSHOT
