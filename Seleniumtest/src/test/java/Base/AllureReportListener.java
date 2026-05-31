package Base;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import java.io.IOException;
import java.io.File;

/**
 * Custom TestNG Listener to automatically generate and open Allure reports
 * after test suite execution completes.
 */
public class AllureReportListener implements ISuiteListener {

    private static final String ALLURE_RESULTS_DIR = "target/allure-results";
    private static final String ALLURE_REPORT_DIR = "target/allure-report";

    @Override
    public void onStart(ISuite suite) {
        System.out.println("========== Test Suite Started: " + suite.getName() + " ==========");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("========== Test Suite Finished: " + suite.getName() + " ==========");
        
        try {
            // Generate Allure report from results
            generateAllureReport();
            
            // Open report in browser
            openAllureReport();
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Error generating or opening Allure report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates Allure report using command line
     */
    private void generateAllureReport() throws IOException, InterruptedException {
        System.out.println("Generating Allure report...");
        
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;
        
        if (os.contains("win")) {
            // Windows command
            pb = new ProcessBuilder("cmd", "/c", "allure", "generate", ALLURE_RESULTS_DIR, 
                                   "-o", ALLURE_REPORT_DIR, "--clean");
        } else {
            // Unix/Linux/Mac command
            pb = new ProcessBuilder("allure", "generate", ALLURE_RESULTS_DIR, 
                                   "-o", ALLURE_REPORT_DIR, "--clean");
        }
        
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();
        
        if (exitCode == 0) {
            System.out.println("✓ Allure report generated successfully!");
        } else {
            System.err.println("✗ Failed to generate Allure report. Exit code: " + exitCode);
        }
    }

    /**
     * Opens the generated Allure report using allure serve (starts web server)
     * This avoids CORS issues that occur when opening file:// URLs directly
     */
    private void openAllureReport() throws IOException, InterruptedException {
        System.out.println("Opening Allure report in browser...");
        
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            
            // Use 'allure serve' instead of direct file:// access to avoid CORS/fetch errors
            if (os.contains("win")) {
                // Windows
                pb = new ProcessBuilder("cmd", "/c", "allure", "serve", ALLURE_RESULTS_DIR);
            } else {
                // Unix/Linux/Mac
                pb = new ProcessBuilder("allure", "serve", ALLURE_RESULTS_DIR);
            }
            
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // Give the server time to start
            Thread.sleep(2000);
            
            System.out.println("✓ Allure report server started on http://localhost:4040");
            System.out.println("✓ Report opening in your default browser...");
            System.out.println("✓ Press Ctrl+C in the terminal to stop the server when done");
            
            // Keep the process running (don't call waitFor immediately)
            // The server stays alive until user terminates it
            
        } catch (Exception e) {
            System.err.println("Could not open report with 'allure serve'.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nManual workaround - run this command in your terminal:");
            System.err.println("allure serve " + ALLURE_RESULTS_DIR);
        }
    }
}
