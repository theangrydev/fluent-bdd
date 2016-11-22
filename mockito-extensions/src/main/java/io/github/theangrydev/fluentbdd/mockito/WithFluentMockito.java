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

import io.github.theangrydev.fluentbdd.core.When;

/**
 * You must implement the {@link #fluentMockito()} method by providing a reference to a {@link FluentMockito} that is
 * a field of your test, annotated with a JUnit {@link org.junit.Rule}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public interface WithFluentMockito<TestResult> extends FluentMockitoCommands<TestResult> {

    /**
     * This should be implemented by referring to a {@link FluentMockito} field that is annotated as a JUnit {@link org.junit.Rule}.
     *
     * @return The delegate {@link FluentMockito}.
     */
    FluentMockito<TestResult> fluentMockito();

    @Override
    default <Mock> FluentMockitoGiven<Mock> given(Mock mock) {
        return fluentMockito().given(mock);
    }

    @Override
    default <Mock> Mock thenVerify(Mock mock) {
        return fluentMockito().thenVerify(mock);
    }
}
