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

import org.mockito.BDDMockito;

/**
 * This is part of a fluent chain to prime a mock that allows writing for example:
 * {@code given(mock).willReturn(42).when().methodIsCalled()}.
 *
 * @param <Mock> The type of mock being primed
 *
 * @see FluentMockitoCommands#given(Object) given(Mock)
 * @see FluentMockitoGivenCommand
 */
public class FluentMockitoGiven<Mock> {
    private final Mock mock;

    FluentMockitoGiven(Mock mock) {
        this.mock = mock;
    }

    /**
     * Continue the fluent chain by specifying that the mock will return the given result.
     * This delegates to {@link BDDMockito#willReturn(Object)}.
     *
     * @param result The result that will be returned
     * @return The next step in the fluent chain
     */
    public FluentMockitoGivenCommand<Mock> willReturn(Object result) {
        return new FluentMockitoGivenCommand<>(mock, BDDMockito.willReturn(result));
    }

    /**
     * Continue the fluent chain by specifying that the mock will not return anything.
     * This delegates to {@link BDDMockito#willDoNothing()}.
     *
     * @return The next step in the fluent chain
     */
    public FluentMockitoGivenCommand<Mock> willDoNothing() {
        return new FluentMockitoGivenCommand<>(mock, BDDMockito.willDoNothing());
    }
}
