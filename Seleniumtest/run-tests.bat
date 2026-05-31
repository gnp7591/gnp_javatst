@echo off
REM TestNG Suite Runner - Windows Batch Script
REM This script makes it easier to run different test suites without worrying about PowerShell syntax

REM Check if Maven is installed
where mvn >nul 2>nul
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

echo.
echo ============================================
echo TestNG Suite Runner for Selenium Tests
echo ============================================
echo.
echo Available Suites:
echo 1. Smoke Tests (6 tests, ~2-3 minutes)
echo 2. Grouped Tests (28 tests, ~15-20 minutes)
echo 3. All UiTests (all tests, ~20-25 minutes)
echo 4. Default Suite (all tests, ~20-25 minutes)
echo 5. Run Specific Test Class
echo 0. Exit
echo.

set /p choice="Select an option (0-5): "

if "%choice%"=="1" (
    echo.
    echo Running Smoke Tests...
    echo.
    mvn clean test -DsuiteXmlFile="testng-smoke.xml"
    goto end
)

if "%choice%"=="2" (
    echo.
    echo Running Grouped Tests...
    echo.
    mvn clean test -DsuiteXmlFile="testng-grouped.xml"
    goto end
)

if "%choice%"=="3" (
    echo.
    echo Running All UiTests...
    echo.
    mvn clean test -DsuiteXmlFile="testng-all-uitests.xml"
    goto end
)

if "%choice%"=="4" (
    echo.
    echo Running Default Suite...
    echo.
    mvn clean test
    goto end
)

if "%choice%"=="5" (
    echo.
    set /p testclass="Enter test class name (e.g., UiTests.AllItemsTest): "
    if "%testclass%"=="" (
        echo ERROR: Test class name cannot be empty
        pause
        exit /b 1
    )
    echo.
    echo Running %testclass%...
    echo.
    mvn clean test -Dtest=%testclass%
    goto end
)

if "%choice%"=="0" (
    echo Exiting...
    exit /b 0
)

echo ERROR: Invalid choice. Please select 0-5
pause
exit /b 1

:end
echo.
echo ============================================
echo Test execution completed!
echo ============================================
echo.
echo To view Allure reports, run:
echo mvn allure:serve
echo.
pause
