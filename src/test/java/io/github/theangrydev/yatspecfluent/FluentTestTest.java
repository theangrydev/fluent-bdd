package io.github.theangrydev.yatspecfluent;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FluentTestTest extends FluentTest<FluentTestTest.Request, FluentTestTest.Response> implements WithAssertions {

    private final TestSystem testSystem = new TestSystem();
    private final ThenFactory<TestAssertions, Response> testAssertions = TestAssertions::new;
    private final SomeDependency someDependency = mock(SomeDependency.class);
    private final AnotherDependency anotherDependency = mock(AnotherDependency.class);

    private static class TestSystem implements When<Request, Response> {
        private final Request request = new Request();
        private final Response response = new Response();

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response response(Request request) {
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
    interface SomeDependency extends Given {};
    interface AnotherDependency extends Given {};

    @Test
    public void responseIsPassedToTheAssertions() {
        given(someDependency);
        when(testSystem);
        TestAssertions then = then(testAssertions);
        assertThat(then.response).isSameAs(testSystem.response);
    }

    @Test
    public void firstGivenIsPrimedAfterWhen() {
        given(someDependency);
        when(testSystem);
        verify(someDependency).prime();
        then(testAssertions);
    }

    @Test
    public void subsequentGivensArePrimedAfterThePreviousGiven() {
        given(someDependency);
        and(anotherDependency);
        verify(someDependency).prime();
        when(testSystem);
        then(testAssertions);
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
