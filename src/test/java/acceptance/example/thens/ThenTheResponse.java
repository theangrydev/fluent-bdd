package acceptance.example.thens;

import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

import java.io.IOException;

public class ThenTheResponse implements WithAssertions {

    private final String responseBody;

    public ThenTheResponse(Response response) {
        responseBody = responseBody(response);
    }

    private String responseBody(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read body from response: " + response);
        }
    }

    public ThenTheResponse isEqualTo(String response) {
        assertThat(responseBody).isEqualTo(response);
        return this;
    }
}
