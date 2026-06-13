package apiTests.apiHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PayloadReader {

    private static final String PAYLOAD_BASE_PATH = "src/test/resources/payloads/";

    // ─── Private constructor — static utility class, not meant to be instantiated ───
    private PayloadReader() {}

    /**
     * Reads the entire JSON file and returns it as a JSONObject.
     *
     * @param fileName  e.g. "apipayload1.json"
     * @return          full JSONObject of the file
     */
    public static JSONObject readFile(String fileName) {
        String fullPath = PAYLOAD_BASE_PATH + fileName;
        try {
            String content = new String(Files.readAllBytes(Paths.get(fullPath)));
            return new JSONObject(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read payload file: " + fullPath, e);
        }
    }

    /**
     * Reads a specific key/block from a payload JSON file.
     * Example: PayloadReader.get("apipayload1.json", "createPost")
     *
     * @param fileName  e.g. "apipayload1.json"
     * @param key       top-level key inside the JSON, e.g. "createPost"
     * @return          JSONObject for that key
     */
    public static JSONObject get(String fileName, String key) {
        return readFile(fileName).getJSONObject(key);
    }

    /**
     * Reads a specific key and returns it as a JSON string (for direct use in .body()).
     *
     * @param fileName  e.g. "apipayload1.json"
     * @param key       top-level key inside the JSON
     * @return          JSON string representation of the block
     */
    public static String getAsString(String fileName, String key) {
        return get(fileName, key).toString();
    }

    /**
     * Reads a nested value (String) from a payload block.
     * Example: PayloadReader.getString("apipayload1.json", "createPost", "title")
     *
     * @param fileName  payload file name
     * @param key       top-level block key
     * @param field     field inside that block
     * @return          String value of the field
     */
    public static String getString(String fileName, String key, String field) {
        return get(fileName, key).getString(field);
    }

    /**
     * Reads a nested integer value from a payload block.
     * Example: PayloadReader.getInt("apipayload1.json", "createPost", "userId")
     */
    public static int getInt(String fileName, String key, String field) {
        return get(fileName, key).getInt(field);
    }
}
