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

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * If you do not want to extend {@link YatspecFluent} as a base class for your tests, you can alternatively use this.
 * You must implement the {@link #yatspecFluent()} method by providing a reference to a {@link YatspecFluent} that is
 * a field of your test, annotated with a JUnit {@link org.junit.Rule}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
@SuppressWarnings("PMD.TooManyMethods") // This is part of the API design
public interface WithYatspecFluent<TestResult> extends YatspecFluentCommands<TestResult> {

    /**
     * This should be implemented by referring to a {@link YatspecFluent} field that is annotated as a JUnit {@link org.junit.Rule}.
     *
     * @return The delegate {@link YatspecFluent}.
     */
    YatspecFluent<TestResult> yatspecFluent();

    @Override
    default Statement apply(Statement base, Description description) {
        return yatspecFluent().apply(base, description);
    }

    @Override
    default TestState testState() {
        return yatspecFluent().testState();
    }

    @Override
    default void given(Given given) {
        yatspecFluent().given(given);
    }

    @Override
    default void and(Given given) {
        yatspecFluent().and(given);
    }

    @Override
    default <T extends When<TestResult>> void when(T when) {
        yatspecFluent().when(when);
    }

    @Override
    default void given(When<TestResult> when) {
        yatspecFluent().given(when);
    }

    @Override
    default void and(When<TestResult> when) {
        yatspecFluent().and(when);
    }

    @Override
    default <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        return yatspecFluent().then(thenAssertion);
    }

    @Override
    default <Then> Then and(ThenAssertion<Then, TestResult> thenAssertion) {
        return yatspecFluent().and(thenAssertion);
    }

    @Override
    default void and(ThenVerification<TestResult> thenVerification) {
        yatspecFluent().and(thenVerification);
    }

    @Override
    default void then(ThenVerification<TestResult> thenVerification) {
        yatspecFluent().then(thenVerification);
    }
}
