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