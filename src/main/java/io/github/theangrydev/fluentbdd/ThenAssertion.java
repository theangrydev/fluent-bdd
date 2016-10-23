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
package io.github.theangrydev.fluentbdd;

/**
 * Implementations should produce a {@link Then} given a {@link TestResult}.
 *
 * This class should provide assertion methods (ideally in the style of AssertJ).
 *
 * @see <a href="http://joel-costigliola.github.io/assertj/">AssertJ</a>
 *
 * @param <Then> The fluent assertions type
 * @param <TestResult> The test result that the {@link Then} operates on
 */
@FunctionalInterface
public interface ThenAssertion<Then, TestResult> {

    /**
     * @param testResult The result from the system under test
     * @return A fluent assertions class that operates on the given {@link TestResult}
     */
    Then then(TestResult testResult);
}
