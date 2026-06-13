package apiTests.tests;

import Base.AllureReportListener;
import apiTests.apiBase.ApiBaseClass;

import apiTests.apiHelper.PayloadReader;
import apiTests.apiHelper.RequestHelper;
import apiTests.apiHelper.ResponseHelper;
import apiTests.apiHelper.SchemaValidator;
import groovy.util.logging.Slf4j;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * PostsApiTests — Week 1 / JSONPlaceholder
 * All HTTP calls go through RequestHelper.
 * All response checks go through ResponseHelper.
 * All schema checks go through SchemaValidator.
 * All request bodies come from PayloadReader.
 */
@Slf4j
@Listeners(AllureReportListener.class)
@Epic("API Automation Tests week 1")
@Feature("API Tests for Practice")
public class PostsApiTests extends ApiBaseClass {

    private static final String PAYLOAD_FILE = "apipayload1.json";
    private static final String SCHEMA_FILE  = "ValidSchema1.json";

    // ─── GET ─────────────────────────────────────────────────────────────────────

    @Test(groups = "Sanity_API",description = "All Posts should return 200")
    public void getAllPosts_shouldReturn200AndHave100Items() {
        Response response = RequestHelper.get(reqSpec, "/posts");

        ResponseHelper.assertStatusCode(response, 200);
        ResponseHelper.assertListSize(response, "$", 100);
        ResponseHelper.assertResponseTime(response, 3000);
    }

    @Test(groups = "Sanity_API",description = "PostById should return 200")
    public void getPostById_shouldReturn200AndMatchSchema() {
        Response response = RequestHelper.get(reqSpec, "/posts/1");

        ResponseHelper.assertStatusCode(response, 200);
        SchemaValidator.validate(response, SCHEMA_FILE);     // schema check via helper
    }

    @Test(groups = "Sanity_API",description = "PostById should return 200 with correct fields")
    public void getPostById_shouldReturn200AndHaveCorrectFields() {
        Response response = RequestHelper.get(reqSpec, "/posts/1");

        ResponseHelper.assertStatusCode(response, 200);
        ResponseHelper.assertField(response, "id", 1);          // int overload
        ResponseHelper.assertNotNull(response, "title");
        ResponseHelper.assertNotNull(response, "body");
        ResponseHelper.assertHeader(response, "Content-Type", "application/json");
    }

    @Test(groups = "Sanity_API",description = "PostById should return Filtered Results")
    public void getPostsByUserId_shouldReturnFilteredResults() {
        // GET /posts?userId=1
        Response response = RequestHelper.get(reqSpec, "/posts", "userId", 1);

        ResponseHelper.assertStatusCode(response, 200);
        ResponseHelper.assertListSize(response, "$", 10);   // user 1 has 10 posts
    }

    @Test(groups = "Sanity_API",description = "PostById should return 404 for invalid content")
    public void getInvalidPost_shouldReturn404() {
        Response response = RequestHelper.get(reqSpec, "/posts/9999");

        ResponseHelper.assertStatusCode(response, 404);
    }

    // ─── POST ────────────────────────────────────────────────────────────────────

    @Test(groups = "Sanity_API",description = "PostById should return 201 with fields")
    public void createPost_shouldReturn201AndEchoFields() {
        // Pull body from payload file
        String body = PayloadReader.getAsString(PAYLOAD_FILE, "createPost");

        Response response = RequestHelper.post(reqSpec, "/posts", body);

        ResponseHelper.assertStatusCode(response, 201);
        ResponseHelper.assertNotNull(response, "id");

        // Assert echoed fields match exactly what was sent
        ResponseHelper.assertField(response, "title",
                PayloadReader.getString(PAYLOAD_FILE, "createPost", "title"));
        ResponseHelper.assertField(response, "userId",
                PayloadReader.getInt(PAYLOAD_FILE, "createPost", "userId"));
    }

    // ─── PUT ─────────────────────────────────────────────────────────────────────

    @Test(groups = "Sanity_API",description = "PostById should return 200 for update/put")
    public void updatePost_shouldReturn200AndReflectUpdatedFields() {
        String body = PayloadReader.getAsString(PAYLOAD_FILE, "updatePost");

        Response response = RequestHelper.put(reqSpec, "/posts/1", body);

        ResponseHelper.assertStatusCode(response, 200);
        ResponseHelper.assertField(response, "title",
                PayloadReader.getString(PAYLOAD_FILE, "updatePost", "title"));
        ResponseHelper.assertField(response, "id", 1);
    }

    // ─── PATCH ───────────────────────────────────────────────────────────────────

    @Test(groups = "Sanity_API",description = "PostById should return 200 for patch")
    public void patchPost_shouldReturn200AndUpdateOnlyTitle() {
        String body = PayloadReader.getAsString(PAYLOAD_FILE, "patchPost");

        Response response = RequestHelper.patch(reqSpec, "/posts/1", body);

        ResponseHelper.assertStatusCode(response, 200);
        ResponseHelper.assertField(response, "title",
                PayloadReader.getString(PAYLOAD_FILE, "patchPost", "title"));
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    @Test(groups = "Sanity_API",description = "PostById should return 200 for delete")
    public void deletePost_shouldReturn200() {
        Response response = RequestHelper.delete(reqSpec, "/posts/1");

        ResponseHelper.assertStatusCode(response, 200);
    }
}