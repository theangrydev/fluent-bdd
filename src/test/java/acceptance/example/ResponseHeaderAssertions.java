package acceptance.example;

import org.assertj.core.api.WithAssertions;

public class ResponseHeaderAssertions implements WithAssertions {

    private final Response response;

    public ResponseHeaderAssertions(Response response) {
        this.response = response;
    }

    public ResponseHeaderAssertions containsContentLength() {
        assertThat(response.getHeaders()).contains("");
        return this;
    }
}
