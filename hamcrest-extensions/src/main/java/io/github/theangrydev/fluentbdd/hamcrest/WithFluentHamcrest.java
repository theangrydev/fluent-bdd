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

import io.github.theangrydev.fluentbdd.core.When;
import io.github.theangrydev.fluentbdd.core.WithFluentBdd;
import org.hamcrest.Matcher;

import static org.junit.Assert.assertThat;

/**
 * This acts as a bridge to use BDD language with Hamcrest matchers.
 *
 * @param <TestResult> The type of test result produced by the {@link When}.
 * @see <a href="http://hamcrest.org/">Hamcrest</a>
 */
public interface WithFluentHamcrest<TestResult> extends WithFluentBdd<TestResult> {

    /**
     * Assert that the given result matches the given matcher.
     *
     * @param result  The result to assert against. You can use {@link #theResult()} (or a private method that extracts something from the result)
     * @param matcher The matcher that should match the result
     * @param <T>     The type of result
     */
    default <T> void then(T result, Matcher<T> matcher) {
        fluentBdd().verification.recordThen(matcher);
        assertThat(result, matcher);
    }

    /**
     * Same as {@link #then(Object, Matcher)}.
     * <p>
     * Assert that the given result matches the given matcher.
     *
     * @param result  The result to assert against. You can use {@link #theResult()} (or a private method that extracts something from the result)
     * @param matcher The matcher that should match the result
     * @param <T>     The type of result
     */
    default <T> void and(T result, Matcher<T> matcher) {
        then(result, matcher);
    }
}
