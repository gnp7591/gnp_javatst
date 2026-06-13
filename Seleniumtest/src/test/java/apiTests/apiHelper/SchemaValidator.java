package apiTests.apiHelper;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.File;

/**
 * Utility to validate API responses against JSON Schema files.
 * Used in tests via: SchemaValidator.validate(response, "ValidSchema1.json")
 */
public class SchemaValidator {

    private static final String SCHEMA_BASE_PATH = "src/test/resources/schemas/";

    // ─── Private constructor — static utility class ───
    private SchemaValidator() {}

    /**
     * Validates a RestAssured Response body against a JSON Schema file.
     * Throws AssertionError if validation fails (handled by RestAssured internally).
     *
     * @param response    the RestAssured response object from your API call
     * @param schemaFile  e.g. "ValidSchema1.json"
     */
    public static void validate(Response response, String schemaFile) {
        File schema = resolveSchema(schemaFile);
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schema));
    }

    /**
     * Returns a Hamcrest matcher for inline use inside .then().body(...)
     * Useful when you want to chain schema validation with other assertions.
     *
     * Example:
     *   given().spec(reqSpec)
     *     .when().get("/posts/1")
     *     .then()
     *       .statusCode(200)
     *       .body(SchemaValidator.matcher("ValidSchema1.json"));
     *
     * @param schemaFile  e.g. "ValidSchema1.json"
     * @return            Hamcrest matcher for use inside .body()
     */
    public static org.hamcrest.Matcher<?> matcher(String schemaFile) {
        File schema = resolveSchema(schemaFile);
        return JsonSchemaValidator.matchesJsonSchema(schema);
    }

    /**
     * Validates a raw JSON string against a schema file.
     * Useful when you have the response body extracted as a String.
     *
     * @param jsonString  raw JSON string to validate
     * @param schemaFile  schema file name
     */
    public static void validateString(String jsonString, String schemaFile) {
        File schema = resolveSchema(schemaFile);
        org.hamcrest.MatcherAssert.assertThat(
                jsonString,
                JsonSchemaValidator.matchesJsonSchema(schema)
        );
    }

    // ─── Internal helper ─────────────────────────────────────────────────────────
    private static File resolveSchema(String schemaFile) {
        File schema = new File(SCHEMA_BASE_PATH + schemaFile);
        if (!schema.exists()) {
            throw new RuntimeException("Schema file not found: " + schema.getAbsolutePath());
        }
        return schema;
    }
}
