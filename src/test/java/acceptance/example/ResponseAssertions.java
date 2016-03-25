package acceptance.example;

import org.assertj.core.api.WithAssertions;

public class ResponseAssertions implements WithAssertions {

    private final Response response;

    public ResponseAssertions(Response response) {
        this.response = response;
    }

    public ResponseAssertions theResponse() {
        return this;
    }

    public ResponseAssertions contains(String content) {
        assertThat(response.getBody()).contains(content);
        return this;
    }

    public ResponseAssertions isEqualTo(String response) {
        assertThat(response).isEqualTo(response);
        return this;
    }

    public ResponseHeaderAssertions theResponseHeader() {
        return new ResponseHeaderAssertions(response);
    }
}
