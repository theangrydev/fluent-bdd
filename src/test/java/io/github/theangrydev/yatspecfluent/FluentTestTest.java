package io.github.theangrydev.yatspecfluent;

import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FluentTestTest extends FluentTest<FluentTestTest.Request, FluentTestTest.Response> implements WithAssertions {

    private final ThenFactory<TestAssertions, Response> testAssertions = TestAssertions::new;
    private final TestSystem testSystem = mock(TestSystem.class);
    private final SomeDependency someDependency = mock(SomeDependency.class);
    private final AnotherDependency anotherDependency = mock(AnotherDependency.class);
    private final Request request = new Request();
    private final Response response = new Response();

    private static class TestAssertions {

        final Response response;

        TestAssertions(Response response) {
            this.response = response;
        }

    }

    static class Response {}
    static class Request {}
    private interface TestSystem extends When<Request,Response> {}
    private interface SomeDependency extends Given {}
    private interface AnotherDependency extends Given {}

    @Before
    public void setUp() {
        Mockito.when(testSystem.request()).thenReturn(request);
        Mockito.when(testSystem.response(request)).thenReturn(response);
    }

    @Test
    public void responseIsPassedToTheAssertions() {
        given(someDependency);
        when(testSystem);
        TestAssertions then = then(testAssertions);
        assertThat(then.response).isSameAs(response);
    }

    @Test
    public void firstGivenIsPrimedAfterGiven() {
        given(someDependency);
        verify(someDependency).prime();
        when(testSystem);
        then(testAssertions);
    }

    @Test
    public void subsequentGivensArePrimedAfterThem() {
        given(someDependency);
        and(anotherDependency);
        verify(anotherDependency).prime();
        when(testSystem);
        then(testAssertions);
    }

    @Test
    public void whenIsPrimedAfterWhen() {
        given(someDependency);
        and(anotherDependency);
        when(testSystem);
        verify(testSystem).response(request);
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
