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
package io.github.theangrydev.fluentbdd.core;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * You must implement the {@link #fluentBdd()} method by providing a reference to a {@link FluentBdd} that is
 * a field of your test, annotated with a JUnit {@link org.junit.Rule}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public interface WithFluentBdd<TestResult> extends FluentBddCommands<TestResult> {

    /**
     * This should be implemented by referring to a {@link FluentBdd} field that is annotated as a JUnit {@link org.junit.Rule}.
     *
     * @return The delegate {@link FluentBdd}.
     */
    FluentBdd<TestResult> fluentBdd();

    @Override
    default Statement apply(Statement base, Description description) {
        return fluentBdd().apply(base, description);
    }

    @Override
    default void given(Given given) {
        fluentBdd().given(given);
    }

    @Override
    default <T extends When<TestResult>> void when(T when) {
        fluentBdd().when(when);
    }

    @Override
    default <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        return fluentBdd().then(thenAssertion);
    }

    @Override
    default void then(ThenVerification<TestResult> thenVerification) {
        fluentBdd().then(thenVerification);
    }
}
