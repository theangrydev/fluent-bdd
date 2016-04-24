package io.github.theangrydev.yatspecfluent;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class FluentTestTest extends FluentTest<FluentTestTest.TestInfrastructure, FluentTestTest.Request, FluentTestTest.Response> implements WithAssertions {

    private final TestSystem testSystem = new TestSystem();
    private final ThenFactory<TestAssertions, Response> testAssertions = TestAssertions::new;
    private final SomeDependency someDependency = new SomeDependency();
    private TestInfrastructure testInfrastructure;

    public FluentTestTest() {
        super(new TestInfrastructure());
    }

    @Override
    protected void setUp(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;
        assertThat(testInfrastructure).isInstanceOf(TestInfrastructure.class);
    }

    @Override
    protected void tearDown(TestInfrastructure testInfrastructure) {
        assertThat(testInfrastructure).isEqualTo(testInfrastructure);
    }

    private static class TestSystem implements When<TestInfrastructure, Request, Response> {
        private final Request request = new Request();
        private final Response response = new Response();
        private TestInfrastructure requestTestInfrastructure;
        private TestInfrastructure callTestInfrastructure;

        @Override
        public Request request(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure) {
            this.requestTestInfrastructure = testInfrastructure;
            return request;
        }

        @Override
        public Response call(Request request, ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure) {
            this.callTestInfrastructure = testInfrastructure;
            return response;
        }
    }

    static class TestAssertions {
        final Response response;

        TestAssertions(Response response) {
            this.response = response;
        }
    }
    static class Response {}
    static class Request {}
    static class TestInfrastructure {}

    private class SomeDependency implements Given<TestInfrastructure> {
        private TestInfrastructure testInfrastructure;
        private ReadOnlyTestItems readOnlyTestItems;

        @Override
        public void prime(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure) {
            this.testInfrastructure = testInfrastructure;
            this.readOnlyTestItems = readOnlyTestItems;
        }
    }

    @Test
    public void systemUnderTestIsCalledWithTestInfrastructure() {
        given(someDependency);
        when(testSystem);
        then(testAssertions);
        assertThat(testSystem.requestTestInfrastructure).isSameAs(testInfrastructure);
        assertThat(testSystem.callTestInfrastructure).isSameAs(testInfrastructure);
    }

    @Test
    public void responseIsPassedToTheAssertions() {
        given(someDependency);
        when(testSystem);
        TestAssertions then = then(testAssertions);
        assertThat(then.response).isSameAs(testSystem.response);
    }

    @Test
    public void dependencyPrimeIsCalledWithInterestingGivensPrimerAndTestInfrastructure() {
        given(someDependency);
        when(testSystem);
        then(testAssertions);
        assertThat(someDependency.testInfrastructure).isSameAs(testInfrastructure);
        assertThat(someDependency.readOnlyTestItems).isSameAs(this);
    }

    @Test
    public void callingGivenAfterWhenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
        then(testAssertions);
    }

    @Test
    public void callingGivenAfterThenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            then(testAssertions);
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void callingThenBeforeWhenIsNotAllowed() {
        assertThatThrownBy(() -> then(testAssertions)).hasMessage("The initial 'then' should be after the 'when'");
        when(testSystem);
        then(testAssertions);
    }

    @Test
    public void callingThenMoreThanOnceIsNotAllowed() {
        assertThatThrownBy(() -> {
            given(someDependency);
            when(testSystem);
            then(testAssertions);
            then(testAssertions);
        }).hasMessage("After the first 'then' you should use 'and'");
    }
}
