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
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;

/**
 * These are some commands used to integrate with yatspec.
 */
public interface FluentYatspecCommands extends WithTestState, WriteOnlyTestItems {

    /**
     * You should aim to never access the state directly, but you might need to (e.g. global shared state).
     * Call {@link #addToGivens(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     * Call {@link #addToCapturedInputsAndOutputs(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     */
    @Override
    TestState testState();

    @Override
    default void addToGivens(String key, Object instance) {
        testState().interestingGivens.add(key, instance);
    }

    @Override
    default void addToCapturedInputsAndOutputs(String key, Object instance) {
        testState().capturedInputAndOutputs.add(key, instance);
    }
}
