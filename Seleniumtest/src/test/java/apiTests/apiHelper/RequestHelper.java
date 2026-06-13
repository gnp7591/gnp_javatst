package apiTests.apiHelper;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Utility to fire HTTP requests cleanly from test classes.
 * Wraps RestAssured's given/when/then so tests stay readable.
 *
 * Used in tests via:
 *   Response res = RequestHelper.get(reqSpec, "/posts/1");
 *   Response res = RequestHelper.post(reqSpec, "/posts", payload);
 */
public class RequestHelper {

    // ─── Private constructor — static utility class ───
    private RequestHelper() {}

    // ════════════════════════════════════════════════════
    //  GET
    // ════════════════════════════════════════════════════

    /**
     * Fires a GET request and returns the Response.
     *
     * @param spec      your RequestSpecification (from BaseTest)
     * @param endpoint  e.g. "/posts" or "/posts/1"
     */
    public static Response get(RequestSpecification spec, String endpoint) {
        return given()
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Fires a GET request with a single query parameter.
     * Example: RequestHelper.get(spec, "/posts", "userId", 1)
     */
    public static Response get(RequestSpecification spec, String endpoint,
                               String paramKey, Object paramValue) {
        return given()
                .spec(spec)
                .queryParam(paramKey, paramValue)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    // ════════════════════════════════════════════════════
    //  POST
    // ════════════════════════════════════════════════════

    /**
     * Fires a POST request with a JSON body string and returns the Response.
     *
     * @param spec      your RequestSpecification
     * @param endpoint  e.g. "/posts"
     * @param body      JSON string — use PayloadReader.getAsString(...) to get this
     */
    public static Response post(RequestSpecification spec, String endpoint, String body) {
        return given()
                .spec(spec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Fires a POST request with a POJO body (serialized to JSON by RestAssured).
     * Useful when using your PostPayload POJO directly.
     */
    public static Response post(RequestSpecification spec, String endpoint, Object pojoBody) {
        return given()
                .spec(spec)
                .body(pojoBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    // ════════════════════════════════════════════════════
    //  PUT
    // ════════════════════════════════════════════════════

    /**
     * Fires a PUT request with a JSON body string.
     *
     * @param spec      your RequestSpecification
     * @param endpoint  e.g. "/posts/1"
     * @param body      JSON string
     */
    public static Response put(RequestSpecification spec, String endpoint, String body) {
        return given()
                .spec(spec)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Fires a PUT request with a POJO body.
     */
    public static Response put(RequestSpecification spec, String endpoint, Object pojoBody) {
        return given()
                .spec(spec)
                .body(pojoBody)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    // ════════════════════════════════════════════════════
    //  PATCH
    // ════════════════════════════════════════════════════

    /**
     * Fires a PATCH request with a partial JSON body string.
     * Example: partially update a post title.
     */
    public static Response patch(RequestSpecification spec, String endpoint, String body) {
        return given()
                .spec(spec)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    // ════════════════════════════════════════════════════
    //  DELETE
    // ════════════════════════════════════════════════════

    /**
     * Fires a DELETE request.
     *
     * @param spec      your RequestSpecification
     * @param endpoint  e.g. "/posts/1"
     */
    public static Response delete(RequestSpecification spec, String endpoint) {
        return given()
                .spec(spec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}
