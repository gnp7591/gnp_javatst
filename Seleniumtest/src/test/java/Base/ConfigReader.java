package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader class is responsible for reading configuration properties from config.properties file
 * This class provides static methods to access configuration values throughout the test execution
 */
public class ConfigReader {
    private static Properties properties;
    private static final String PROPERTIES_FILE_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    /**
     * Load properties from config.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            System.err.println("Failed to load properties file: " + PROPERTIES_FILE_PATH);
            e.printStackTrace();
        }
    }

    /**
     * Get property value by key
     * @param key - Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with default value if key doesn't exist
     * @param key - Property key
     * @param defaultValue - Default value if key not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // ========== Application URLs ==========
    public static String getApplicationUrl() {
        return getProperty("app.url");
    }

    public static String getInventoryUrl() {
        return getProperty("app.inventory.url");
    }

    // ========== User Credentials ==========
    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    public static String getValidPassword() {
        return getProperty("valid.password");
    }

    public static String getInvalidUsername() {
        return getProperty("invalid.username");
    }

    public static String getInvalidPassword() {
        return getProperty("invalid.password");
    }

    public static String getEmptyUsername() {
        return getProperty("empty.username");
    }

    public static String getEmptyPassword() {
        return getProperty("empty.password");
    }

    // ========== Browser Configuration ==========
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static boolean isBrowserMaximized() {
        return Boolean.parseBoolean(getProperty("browser.maximize", "true"));
    }

    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(getProperty("headless.mode", "false"));
    }

    // ========== Wait Times ==========
    public static long getImplicitWait() {
        return Long.parseLong(getProperty("implicit.wait", "10"));
    }

    public static long getExplicitWait() {
        return Long.parseLong(getProperty("explicit.wait", "10"));
    }

    public static long getPageLoadTimeout() {
        return Long.parseLong(getProperty("page.load.timeout", "30"));
    }

    // ========== Test Execution Configuration ==========
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    // ========== Allure Configuration ==========
    public static String getAllureResultsDir() {
        return getProperty("allure.results.dir");
    }

    public static String getAllureReportDir() {
        return getProperty("allure.report.dir");
    }

}
