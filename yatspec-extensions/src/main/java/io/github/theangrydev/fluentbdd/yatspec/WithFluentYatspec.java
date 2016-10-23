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
package io.github.theangrydev.fluentbdd.yatspec;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import io.github.theangrydev.fluentbdd.core.When;
import io.github.theangrydev.fluentbdd.core.WithFluentBdd;

/**
 * If you do not want to extend {@link FluentYatspec} as a base class for your tests, you can alternatively use this.
 * You must implement the {@link #fluentBdd()} method by providing a reference to a {@link FluentYatspec} that is
 * a field of your test, annotated with a JUnit {@link org.junit.Rule}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public interface WithFluentYatspec<TestResult> extends WithFluentBdd<TestResult>, FluentYatspecCommands {

    /**
     * This should be implemented by referring to a {@link FluentYatspec} field that is annotated as a JUnit {@link org.junit.Rule}.
     *
     * @return The delegate {@link FluentYatspec}.
     */
    @Override
    FluentYatspec<TestResult> fluentBdd();

    @Override
    default TestState testState() {
        return fluentBdd().testState();
    }
}
