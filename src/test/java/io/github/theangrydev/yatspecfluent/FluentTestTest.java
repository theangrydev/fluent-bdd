/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of yatspec-fluent.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.theangrydev.yatspecfluent;

import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static java.lang.String.format;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FluentTestTest extends FluentTest<FluentTestTest.Request, FluentTestTest.Response> implements WithAssertions {

    private final ThenFactory<TestAssertions, Response> testAssertions = TestAssertions::new;
    private final TestSystem testSystem = mock(TestSystem.class);
    private final TestSystem nullRequestTestSystem = mock(TestSystem.class);
    private final TestSystem nullResponseTestSystem = mock(TestSystem.class);
    private final SomeDependency someDependency = mock(SomeDependency.class);
    private final SomeDependency someDependency2 = mock(SomeDependency.class);
    private final AnotherDependency anotherDependency = mock(AnotherDependency.class);
    private final Request request = new Request();
    private final Response response = new Response();

    private boolean given;
    private boolean when;
    private boolean then;

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
        Mockito.when(nullResponseTestSystem.request()).thenReturn(request);
        Mockito.when(testSystem.response(request)).thenReturn(response);
    }

    @After
    public void putTestInValidState() {
        if (!given && !when) {
            given(someDependency);
        }
        if (!when) {
            when(testSystem);
        }
        if (!then) {
            then(testAssertions);
        }
    }

    @Override
    protected void given(Given given) {
        super.given(given);
        this.given = true;
    }

    @Override
    protected <T extends When<Request, Response>> void when(T when) {
        super.when(when);
        this.when = true;
    }

    @Override
    protected <Then> Then then(ThenFactory<Then, Response> thenFactory) {
        Then then = super.then(thenFactory);
        this.then = true;
        return then;
    }

    @Test
    public void nullRequestIsReported() {
        assertThatThrownBy(() -> {
            when(nullRequestTestSystem);
        }).hasMessage(format("'%s' request was null", nullRequestTestSystem));
    }

    @Test
    public void nullResponseIsReported() {
        assertThatThrownBy(() -> {
            when(nullResponseTestSystem);
        }).hasMessage(format("'%s' response was null", nullResponseTestSystem));
    }

    @Test
    public void givensAfterTheFirstOneMustBeAnds() {
        assertThatThrownBy(() -> {
            given(someDependency);
            given(someDependency);
        }).hasMessage("All of the 'given' statements after the initial then should be 'and'");
    }

    @Test
    public void firstThenShouldBeAThen() {
        assertThatThrownBy(() -> {
            when(testSystem);
            and(testAssertions);
        }).hasMessage("The first 'then' should be a 'then' and after that you should use 'and'");
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
    }

    @Test
    public void subsequentGivensArePrimedAfterThem() {
        given(someDependency);
        and(anotherDependency);
        verify(anotherDependency).prime();
    }

    @Test
    public void whenIsPrimedAfterWhen() {
        given(someDependency);
        and(anotherDependency);
        when(testSystem);
        verify(testSystem).response(request);
    }

    @Test
    public void multipleGivensOfTheSameTypeAreAllowed() {
        given(someDependency);
        verify(someDependency).prime();
        and(someDependency2);
        verify(someDependency2).prime();
    }

    @Test
    public void callingSameGivenTwiceIsNotAllowed() {
        assertThatThrownBy(() -> {
            given(someDependency);
            and(someDependency);
        }).hasMessage(format("The dependency '%s' has already specified a 'given' step", someDependency));
    }

    @Test
    public void callingGivenAfterWhenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void multipleWhensAreNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            when(testSystem);
        }).hasMessage("There should only be one 'when', after the 'given' and before the 'then'");
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
