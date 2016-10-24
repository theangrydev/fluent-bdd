/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of fluent-bdd.
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
package io.github.theangrydev.fluentbdd.mockito;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.ThenVerification;
import io.github.theangrydev.fluentbdd.core.WhenWithoutResult;
import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.verification.VerificationInOrderFailure;

import static org.mockito.Mockito.mock;

//TODO: rewrite these tests, they are crap and just for coverage at the moment
public class FluentMockitoTest implements WithFluentMockito<FluentMockitoTest>, WithAssertions {

    @Rule
    public final FluentBdd<FluentMockitoTest> fluentBdd = new FluentBdd<>(this);

    @Rule
    public final FluentMockito<FluentMockitoTest> fluentMockito = new FluentMockito<>(fluentBdd);

    private Dependency dependency = mock(Dependency.class);
    private AnotherDependency anotherDependency = mock(AnotherDependency.class);
    private StupidCode stupidCode = new StupidCode(dependency, anotherDependency);
    private int thing;

    @Override
    public FluentMockito<FluentMockitoTest> fluentMockito() {
        return fluentMockito;
    }

    @Override
    public FluentBdd<FluentMockitoTest> fluentBdd() {
        return fluentBdd;
    }

    @Test
    public void givenAfterWhen() {
        assertThatThrownBy(() -> {
            whenCalling(this::voidThing);
            given(dependency).willReturn(10).when().someMethod("thing");
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");

        // TODO: look at FluentBddTest and see if there is something common that can be extracted around making the test green when in a bad state
        // Just to make the other validation steps pass
        thenVerify(dependency).someMethod("thing");
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void inOrderFailure() {
        whenCalling(this::voidThing);
        thenVerify(dependency).someMethod("thing");
        andVerify(dependency).anotherMethod("bing");
        andVerify(anotherDependency).boringMethod();
    }

    @Test
    public void givenVoidMock() {
        given(dependency).willReturn(10).when().someMethod("thing");
        and(dependency).willReturn(10).when().anotherMethod("bing");
        and(dependency).willDoNothing().when().voidMethod("bing");
        whenCalling(this::voidThing);
        thenVerify(dependency).someMethod("thing");
        andVerify(anotherDependency).boringMethod();
        andVerify(dependency).anotherMethod("bing");
    }

    @Test
    public void givenMock() {
        given(dependency).willReturn(10).when().someMethod("thing");
        and(dependency).willReturn(10).when().anotherMethod("bing");
        whenCalling(theStupidCode().withBar());
        thenVerify(dependency).someMethod("thing");
        andVerify(dependency).anotherMethod("bing");
        and(someAssertion());
    }

    private MyWhenWithoutResult theStupidCode() {
        return new MyWhenWithoutResult();
    }

    private ThenVerification<FluentMockitoTest> someAssertion() {
        return fluentMockitoTest -> assertThat(thing).isLessThan(100);
    }

    private void voidThing() {
        stupidCode.voidThingToTest();
    }

    private class MyWhenWithoutResult implements WhenWithoutResult {
        @Override
        public void execute() {
            thing = stupidCode.thingToTest();
        }

        public MyWhenWithoutResult withBar() {
            return this;
        }
    }
}