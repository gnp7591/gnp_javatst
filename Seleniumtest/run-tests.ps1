# TestNG Suite Runner - PowerShell Script
# This script makes it easier to run different test suites in Windows PowerShell

function Show-Menu {
    Clear-Host
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host "TestNG Suite Runner for Selenium Tests" -ForegroundColor Cyan
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Available Suites:" -ForegroundColor Yellow
    Write-Host "1. Smoke Tests (6 tests, ~2-3 minutes)" -ForegroundColor White
    Write-Host "2. Grouped Tests (28 tests, ~15-20 minutes)" -ForegroundColor White
    Write-Host "3. All UiTests (all tests, ~20-25 minutes)" -ForegroundColor White
    Write-Host "4. Default Suite (all tests, ~20-25 minutes)" -ForegroundColor White
    Write-Host "5. Run Specific Test Class" -ForegroundColor White
    Write-Host "6. Run Specific Test Method" -ForegroundColor White
    Write-Host "7. Generate Allure Report" -ForegroundColor White
    Write-Host "8. Generate and Serve Allure Report" -ForegroundColor White
    Write-Host "0. Exit" -ForegroundColor White
    Write-Host ""
}

function Check-Maven {
    try {
        mvn --version > $null 2>&1
        return $true
    }
    catch {
        return $false
    }
}

function Run-Tests {
    param (
        [string]$Suite,
        [string]$Description
    )
    
    Write-Host ""
    Write-Host "Running: $Description" -ForegroundColor Green
    Write-Host ""
    
    if ($Suite -eq "default") {
        mvn clean test
    }
    else {
        mvn clean test -DsuiteXmlFile="$Suite"
    }
}

function Run-TestClass {
    param (
        [string]$TestClass
    )
    
    Write-Host ""
    Write-Host "Running: $TestClass" -ForegroundColor Green
    Write-Host ""
    
    mvn clean test -Dtest=$TestClass
}

function Run-TestMethod {
    param (
        [string]$TestClass,
        [string]$TestMethod
    )
    
    Write-Host ""
    Write-Host "Running: $TestClass#$TestMethod" -ForegroundColor Green
    Write-Host ""
    
    mvn clean test -Dtest=$TestClass#$TestMethod
}

# Main Script
Clear-Host

# Check Maven
if (-not (Check-Maven)) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven and add it to your PATH" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Main Loop
do {
    Show-Menu
    $choice = Read-Host "Select an option (0-8)"
    
    switch ($choice) {
        "1" { 
            Run-Tests "testng-smoke.xml" "Smoke Tests (6 tests)" 
            Read-Host "Press Enter to continue"
        }
        "2" { 
            Run-Tests "testng-grouped.xml" "Grouped Tests (28 tests)" 
            Read-Host "Press Enter to continue"
        }
        "3" { 
            Run-Tests "testng-all-uitests.xml" "All UiTests" 
            Read-Host "Press Enter to continue"
        }
        "4" { 
            Run-Tests "default" "Default Suite" 
            Read-Host "Press Enter to continue"
        }
        "5" {
            Write-Host ""
            $testClass = Read-Host "Enter test class name (e.g., UiTests.AllItemsTest)"
            if ([string]::IsNullOrWhiteSpace($testClass)) {
                Write-Host "ERROR: Test class name cannot be empty" -ForegroundColor Red
            }
            else {
                Run-TestClass $testClass
                Read-Host "Press Enter to continue"
            }
        }
        "6" {
            Write-Host ""
            $testClass = Read-Host "Enter test class (e.g., UiTests.AllItemsTest)"
            $testMethod = Read-Host "Enter test method (e.g., testAddProductToCart)"
            if ([string]::IsNullOrWhiteSpace($testClass) -or [string]::IsNullOrWhiteSpace($testMethod)) {
                Write-Host "ERROR: Test class and method cannot be empty" -ForegroundColor Red
            }
            else {
                Run-TestMethod $testClass $testMethod
                Read-Host "Press Enter to continue"
            }
        }
        "7" {
            Write-Host ""
            Write-Host "Generating Allure Report..." -ForegroundColor Green
            Write-Host ""
            mvn allure:report
            Write-Host ""
            Write-Host "Report generated in: target/allure-report/" -ForegroundColor Yellow
            Read-Host "Press Enter to continue"
        }
        "8" {
            Write-Host ""
            Write-Host "Generating and Serving Allure Report..." -ForegroundColor Green
            Write-Host ""
            mvn allure:serve
            Read-Host "Press Enter to continue"
        }
        "0" {
            Write-Host "Exiting..." -ForegroundColor Cyan
            exit 0
        }
        default {
            Write-Host "ERROR: Invalid choice. Please select 0-8" -ForegroundColor Red
            Read-Host "Press Enter to continue"
        }
    }
} while ($true)
