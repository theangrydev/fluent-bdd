package io.github.theangrydev.fluentbdd.mockito;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.ThenVerification;
import io.github.theangrydev.fluentbdd.core.When;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import org.assertj.core.api.WithAssertions;

public class FluentMockitoTest implements WithFluentMockito<FluentMockitoTest>, WithAssertions {

    @Rule
    public final FluentBdd<FluentMockitoTest> fluentBdd = new FluentBdd<>();
    private final FluentMockito<FluentMockitoTest> fluentMockito = new FluentMockito<>(fluentBdd);

    private Dependency dependency = mock(Dependency.class);
    private StupidCode stupidCode = new StupidCode(dependency);
    private int thing;

    @Override
    public FluentMockito<FluentMockitoTest> fluentMockito() {
        return fluentMockito;
    }

    @Override
    public FluentBdd<FluentMockitoTest> fluentBdd() {
        return fluentBdd;
    }

    //TODO: what about void whens? what about multiple tests with different results, e.g. verification state plus a method return value??
    @Test
    public void givenVoidMock() {
        given(dependency).willReturn(10).when().someMethod("thing");
        and(dependency).willReturn(10).when().anotherMethod("bing");
        when(voidThingIsCalled());
        thenVerify(dependency).someMethod("thing");
        andThenVerify(dependency).anotherMethod("bing");
    }

    @Test
    public void givenMock() {
        given(dependency).willReturn(10).when().someMethod("thing");
        and(dependency).willReturn(10).when().anotherMethod("bing");
        when(notVoidThingToTest());
        thenVerify(dependency).someMethod("thing");
        andThenVerify(dependency).anotherMethod("bing");
        and(someAssertion());
    }

    private ThenVerification<FluentMockitoTest> someAssertion() {
        return fluentMockitoTest -> assertThat(thing).isLessThan(100);
    }

    private When<FluentMockitoTest> voidThingIsCalled() {
        return () -> {
            stupidCode.voidThingToTest();
            return FluentMockitoTest.this;
        };
    }

    private When<FluentMockitoTest> notVoidThingToTest() {
        return () -> {
            FluentMockitoTest.this.thing = stupidCode.thingToTest();
            return FluentMockitoTest.this;
        };
    }

}