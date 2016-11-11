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
package io.github.theangrydev.fluentbdd.hamcrest;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.Given;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class WithFluentHamcrestTest implements WithFluentHamcrest<WithFluentHamcrestTest> {

    @Rule
    public FluentBdd<WithFluentHamcrestTest> fluentBdd = new FluentBdd<>(this);


    @Override
    public FluentBdd<WithFluentHamcrestTest> fluentBdd() {
        return fluentBdd;
    }

    private interface SomeDependency extends Given {}
    private final SomeDependency someDependency = mock(SomeDependency.class);

    private MutableMatcher mutableMatcher = new MutableMatcher();

    private int intResult;

    @Test(expected = AssertionError.class)
    public void failingThenMatcher() {
        given(someDependency);
        whenCalling(() -> intResult = 42);
        then(intResult, is(lessThan(42)));
    }

    @Test(expected = AssertionError.class)
    public void failingAndMatcher() {
        given(someDependency);
        whenCalling(() -> intResult = 42);
        then(theResult(), is(instanceOf(WithFluentHamcrestTest.class)));
        and(theResult(), is(nullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void mutableMatcherUsedMoreThanOnceIsNotAllowed() {
        given(someDependency);
        whenCalling(() -> intResult = 42);
        then(intResult, mutableMatcher.withThing("42"));
        then(intResult, mutableMatcher);
    }

    private static class MutableMatcher extends TypeSafeMatcher<Integer> {

        private String stringValue;

        public MutableMatcher withThing(String thing) {
            this.stringValue = thing;
            return this;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(stringValue);
        }

        @Override
        protected boolean matchesSafely(Integer item) {
            return String.valueOf(item).equals(stringValue);
        }
    }
}