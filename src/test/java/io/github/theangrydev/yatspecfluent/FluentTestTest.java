package io.github.theangrydev.yatspecfluent;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class FluentTestTest extends FluentTest<FluentTestTest.TestInfrastructure, FluentTestTest.TestSystem, FluentTestTest.Request, FluentTestTest.Response, FluentTestTest.TestAssertions> implements WithAssertions {

    private final TestAssertions testAssertions = new TestAssertions();
    private final SomeDependency someDependency = new SomeDependency();

    public FluentTestTest() {
        super(new TestSystem(), new TestInfrastructure());
    }

    static class TestSystem implements SystemUnderTest<TestInfrastructure, Request, Response> {
        private final Request request = new Request();
        private final Response response = new Response();
        private TestInfrastructure requestTestInfrastructure;
        private TestInfrastructure callTestInfrastructure;

        @Override
        public Request request(TestInfrastructure testInfrastructure) throws Exception {
            this.requestTestInfrastructure = testInfrastructure;
            return request;
        }

        @Override
        public Response call(Request request, TestInfrastructure testInfrastructure) throws Exception {
            this.callTestInfrastructure = testInfrastructure;
            return response;
        }
    }

    static class TestAssertions {}
    static class Response {}
    static class Request {}
    static class TestInfrastructure {}

    private class SomeDependency implements Primer<TestInfrastructure> {
        private TestInfrastructure testInfrastructure;
        private InterestingTestItems interestingTestItems;

        @Override
        public void prime(InterestingTestItems interestingTestItems, TestInfrastructure testInfrastructure) {
            this.testInfrastructure = testInfrastructure;
            this.interestingTestItems = interestingTestItems;
        }
    }

    @Test
    public void systemUnderTestIsCalledWithTestInfrastructure() {
        given(someDependency);
        when();
        then();
        assertThat(systemUnderTest.requestTestInfrastructure).isSameAs(testInfrastructure);
        assertThat(systemUnderTest.callTestInfrastructure).isSameAs(testInfrastructure);
    }

    @Test
    public void dependencyPrimeIsCalledWithInterestingGivensPrimerAndTestInfrastructure() {
        given(someDependency);
        when();
        then();
        assertThat(someDependency.testInfrastructure).isSameAs(testInfrastructure);
        assertThat(someDependency.interestingTestItems).isSameAs(this);
    }

    @Test
    public void callingGivenAfterWhenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when();
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
        then();
    }

    @Test
    public void callingGivenAfterThenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when();
            then();
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void callingThenBeforeWhenIsNotAllowed() {
        assertThatThrownBy(this::then).hasMessage("The initial 'then' should be after the 'when'");
        when();
        then();
    }

    @Test
    public void callingThenMoreThanOnceIsNotAllowed() {
        assertThatThrownBy(() -> {
            given(someDependency);
            when();
            then();
            then();
        }).hasMessage("After the first 'then' you should use 'and'");
    }

    @Override
    protected TestAssertions responseAssertions(Response response) {
        return testAssertions;
    }

    @Override
    protected TestInfrastructure testInfrastructure() {
        return testInfrastructure;
    }

    @Override
    protected void beforeSystemHasBeenCalled(Request request) {
        assertThat(request).isSameAs(systemUnderTest.request);

    }

    @Override
    protected void afterSystemHasBeenCalled(Response response) {
        assertThat(response).isSameAs(systemUnderTest.response);

    }
}
