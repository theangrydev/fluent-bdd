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

/**
 * When the {@link #verify(Object)} method is invoked, a verification should be made about the {@link TestResult}.
 *
 * For example, this could mean verifying a HTTP interaction took place.
 *
 * This class should act as a builder for use in {@link FluentTest}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern#Java_example">The Builder Pattern</a>
 * @param <TestResult> The test result that the {@link #verify(Object)} operates on
 */
@FunctionalInterface
public interface ThenVerification<TestResult> {

    /**
     * Verify that the {@link TestResult} matches the criteria that were built up to make this {@link ThenVerification}.
     *
     * @param testResult The result from the system under test
     */
    void verify(TestResult testResult);
}
