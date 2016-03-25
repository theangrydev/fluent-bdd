package acceptance.example.test;

import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

import java.io.IOException;

public class ResponseAssertions implements WithAssertions {

    private final Response response;
    private final String responseBody;

    public ResponseAssertions(Response response) {
        this.response = response;
        responseBody = responseBody(response);
    }

    private String responseBody(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read body from response: " + response);
        }
    }

    public ResponseAssertions theResponse() {
        return this;
    }

    public ResponseAssertions isEqualTo(String response) {
        assertThat(responseBody).isEqualTo(response);
        return this;
    }

    public ResponseHeaderAssertions theResponseHeaders() {
        return new ResponseHeaderAssertions(response);
    }
}
