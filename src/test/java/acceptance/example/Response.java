package acceptance.example;

public class Response {

    private final String body;

    public Response(String body) {
        this.body = body;
    }

    public String getHeaders() {
        return "headers";
    }

    public String getBody() {
        return body;
    }
}
