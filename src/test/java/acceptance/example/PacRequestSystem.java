package acceptance.example;

import io.github.theangrydev.yatspecfluent.RequestResponse;
import io.github.theangrydev.yatspecfluent.SystemUnderTest;
import org.assertj.core.api.WithAssertions;

public class PacRequestSystem implements SystemUnderTest<TestInfrastructure, Response>, WithAssertions {

    private String bar;
    private String foo;

    @Override
    public RequestResponse<Response> call(TestInfrastructure testInfrastructure) {
        return new RequestResponse<>("request", new Response("response"));
    }

    public void withArgument(String bar) {
        this.bar = bar;
    }

    public PacRequestSystem callsTheEndpoint() {
        return this;
    }
}
