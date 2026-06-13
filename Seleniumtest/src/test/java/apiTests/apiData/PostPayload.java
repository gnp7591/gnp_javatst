package apiTests.apiData;

public class PostPayload {

    private String title;
    private String body;
    private int userId;

    // constructors, getters, setters
    public PostPayload(String title, String body, int userId) {
        this.title  = title;
        this.body   = body;
        this.userId = userId;
    }
    // getters...

}
