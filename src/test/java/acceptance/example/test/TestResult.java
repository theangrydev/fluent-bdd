package acceptance.example.test;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import okhttp3.Response;

/**
 * This is the complete result of a test, which includes a HTTP response and a way to verify outbound HTTP interactions.
 * If there was a database, there would be methods here exposing the database state.
 */
public class TestResult {

    private final TestInfrastructure testInfrastructure;
    public final Response response;

    public TestResult(TestInfrastructure testInfrastructure, Response response) {
        this.testInfrastructure = testInfrastructure;
        this.response = response;
    }

    public void verifyThat(RequestPatternBuilder requestPatternBuilder) {
        testInfrastructure.verifyThat(requestPatternBuilder);
    }
}
