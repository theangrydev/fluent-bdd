package acceptance.example.thens;

import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

public class ThenTheResponseHeaders implements WithAssertions {

    private final Response response;

    public ThenTheResponseHeaders(Response response) {
        this.response = response;
    }

    public ThenTheResponseHeaders contains(String headerName) {
        assertThat(response.headers().names()).contains(headerName);
        return this;
    }
}
