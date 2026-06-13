package apiTests.apiHelper;

import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Utility to extract values and perform assertions on RestAssured Responses.
 * Keeps test code clean by centralizing all response-handling logic.
 *
 * Used in tests via:
 *   int id = ResponseHelper.extractInt(response, "id");
 *   ResponseHelper.assertStatusCode(response, 200);
 */
public class ResponseHelper {

    // ─── Private constructor — static utility class ───
    private ResponseHelper() {}

    // ════════════════════════════════════════════════════
    //  STATUS CODE ASSERTIONS
    // ════════════════════════════════════════════════════

    /**
     * Asserts the response status code matches expected.
     * Prints actual vs expected in failure message for easy debugging.
     */
    public static void assertStatusCode(Response response, int expectedCode) {
        int actual = response.getStatusCode();
        Assert.assertEquals(
                actual,
                expectedCode,
                "Status code mismatch. Expected: " + expectedCode + " | Actual: " + actual
                        + "\nResponse body: " + response.getBody().asString()
        );
    }

    // ════════════════════════════════════════════════════
    //  VALUE EXTRACTION
    // ════════════════════════════════════════════════════

    /**
     * Extracts a String field from the response JSON body using JsonPath.
     * Example: ResponseHelper.extractString(response, "title")
     */
    public static String extractString(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    /**
     * Extracts an Integer field from the response JSON body using JsonPath.
     * Example: ResponseHelper.extractInt(response, "id")
     */
    public static int extractInt(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    /**
     * Extracts a Boolean field from the response JSON body.
     * Example: ResponseHelper.extractBoolean(response, "completed")
     */
    public static boolean extractBoolean(Response response, String jsonPath) {
        return response.jsonPath().getBoolean(jsonPath);
    }

    /**
     * Returns the full response body as a plain String.
     * Useful for logging or raw string assertions.
     */
    public static String getBodyAsString(Response response) {
        return response.getBody().asString();
    }

    // ════════════════════════════════════════════════════
    //  FIELD-LEVEL ASSERTIONS
    // ════════════════════════════════════════════════════

    /**
     * Asserts that a String field in the response matches the expected value.
     * Example: ResponseHelper.assertField(response, "title", "foo")
     */
    public static void assertField(Response response, String jsonPath, String expectedValue) {
        String actual = extractString(response, jsonPath);
        Assert.assertEquals(
                actual,
                expectedValue,
                "Field '" + jsonPath + "' mismatch. Expected: " + expectedValue + " | Actual: " + actual
        );
    }

    /**
     * Asserts that an Integer field in the response matches the expected value.
     * Example: ResponseHelper.assertField(response, "userId", 1)
     */
    public static void assertField(Response response, String jsonPath, int expectedValue) {
        int actual = extractInt(response, jsonPath);
        Assert.assertEquals(
                actual,
                expectedValue,
                "Field '" + jsonPath + "' mismatch. Expected: " + expectedValue + " | Actual: " + actual
        );
    }

    /**
     * Asserts that a field is NOT null in the response.
     * Useful for dynamic fields like auto-generated IDs.
     * Example: ResponseHelper.assertNotNull(response, "id")
     */
    public static void assertNotNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(
                value,
                "Expected field '" + jsonPath + "' to be non-null, but it was null."
        );
    }

    /**
     * Asserts that a field is null or absent in the response.
     */
    public static void assertNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNull(
                value,
                "Expected field '" + jsonPath + "' to be null, but got: " + value
        );
    }

    // ════════════════════════════════════════════════════
    //  ARRAY / LIST ASSERTIONS
    // ════════════════════════════════════════════════════

    /**
     * Asserts the size of a JSON array in the response.
     * Example: ResponseHelper.assertListSize(response, "$", 100)  → root is array of 100
     *          ResponseHelper.assertListSize(response, "comments", 5)
     */
    public static void assertListSize(Response response, String jsonPath, int expectedSize) {
        int actual = response.jsonPath().getList(jsonPath).size();
        Assert.assertEquals(
                actual,
                expectedSize,
                "List size mismatch at '" + jsonPath + "'. Expected: " + expectedSize + " | Actual: " + actual
        );
    }

    /**
     * Returns the size of a JSON array at the given path.
     * Useful when size is dynamic and you just need the count.
     */
    public static int getListSize(Response response, String jsonPath) {
        return response.jsonPath().getList(jsonPath).size();
    }

    // ════════════════════════════════════════════════════
    //  RESPONSE TIME
    // ════════════════════════════════════════════════════

    /**
     * Asserts that the response time is within an acceptable threshold.
     * Example: ResponseHelper.assertResponseTime(response, 2000)  → must be under 2 seconds
     *
     * @param maxMilliseconds  max acceptable response time in ms
     */
    public static void assertResponseTime(Response response, long maxMilliseconds) {
        long actual = response.getTime();
        Assert.assertTrue(
                actual <= maxMilliseconds,
                "Response time exceeded threshold. Max: " + maxMilliseconds
                        + "ms | Actual: " + actual + "ms"
        );
    }

    // ════════════════════════════════════════════════════
    //  HEADER ASSERTIONS
    // ════════════════════════════════════════════════════

    /**
     * Asserts a specific response header value.
     * Example: ResponseHelper.assertHeader(response, "Content-Type", "application/json")
     */
    public static void assertHeader(Response response, String headerName, String expectedValue) {
        String actual = response.getHeader(headerName);
        Assert.assertTrue(
                actual != null && actual.contains(expectedValue),
                "Header '" + headerName + "' mismatch. Expected to contain: "
                        + expectedValue + " | Actual: " + actual
        );
    }
}
