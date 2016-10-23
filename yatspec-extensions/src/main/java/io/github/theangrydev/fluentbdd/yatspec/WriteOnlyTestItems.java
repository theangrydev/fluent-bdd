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

/**
 * This class allows writing to {@link com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs}
 * and {@link com.googlecode.yatspec.state.givenwhenthen.InterestingGivens} but
 * does not allow reading them.
 *
 * This is to encourage you not to have a big bag of global state. Instead, the
 * interesting givens and captured inputs and outputs should just be a way to
 * highlight important information to the person reading the acceptance test.
 */
public interface WriteOnlyTestItems {

    /**
     * @param key The name of the interesting given
     * @param instance The interesting given instance
     */
    void addToGivens(String key, Object instance);

    /**
     * @param key The name of the captured input or output
     * @param instance The captured input or output
     */
    void addToCapturedInputsAndOutputs(String key, Object instance);
}
