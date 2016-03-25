package acceptance.example.test;

import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

public class ResponseHeaderAssertions implements WithAssertions {

    private final Response response;

    public ResponseHeaderAssertions(Response response) {
        this.response = response;
    }

    public ResponseHeaderAssertions contains(String headerName) {
        assertThat(response.headers().names()).contains(headerName);
        return this;
    }
}
