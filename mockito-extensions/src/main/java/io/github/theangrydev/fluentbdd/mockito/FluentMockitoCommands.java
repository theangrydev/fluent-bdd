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
import io.github.theangrydev.fluentbdd.core.WithFluentBdd;

/**
 * These are the Mockito BDD extension methods that are used to write tests.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public interface FluentMockitoCommands<TestResult> extends WithFluentBdd<TestResult> {

    /**
     * Start to prime a {@link org.mockito.Mockito#mock(Class)}.
     *
     * @param mock   The mock to be primed
     * @param <Mock> The type of mock to be primed
     * @return The start of the fluent given chain
     *
     * @see FluentMockitoGiven
     * @see FluentMockitoGivenCommand
     */
    <Mock> FluentMockitoGiven<Mock> given(Mock mock);

    /**
     * Same as {@link #given(Object) given(Mock)}.
     *
     * @param mock   The mock to be primed
     * @param <Mock> The type of mock to be primed
     * @return The start of the fluent given chain
     *
     * @see FluentMockitoGiven
     * @see FluentMockitoGivenCommand
     */
    default <Mock> FluentMockitoGiven<Mock> and(Mock mock) {
        return given(mock);
    }

    /**
     * This performs an {@link org.mockito.Mockito#inOrder(Object...)} verification on the given mock, considering all
     * the mocks that have been constructed in this test.
     *
     * @param mock   The mock to be verified
     * @param <Mock> The type of mock to be verified
     * @return The mock verification chain
     */
    <Mock> Mock thenVerify(Mock mock);

    /**
     * Same as {@link #thenVerify(Object) thenVerify(Mock)}.
     *
     * @param mock   The mock to be verified
     * @param <Mock> The type of mock to be verified
     * @return The mock verification chain
     */
    default <Mock> Mock andVerify(Mock mock) {
        return thenVerify(mock);
    }
}
