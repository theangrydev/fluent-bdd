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

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class FluentYatspecTest implements WithAssertions {

    private final FluentYatspec<TestResult> fluentYatspec = new FluentYatspec<>();

    @Test
    public void extendFluentBdd() {
        assertThat(fluentYatspec).isInstanceOf(FluentBdd.class);
    }

    @Test
    public void implementsFluentYatspecCommands() {
        assertThat(fluentYatspec).isInstanceOf(FluentYatspecCommands.class);
    }

    @Test
    public void hasTestState() {
        assertThat(fluentYatspec.testState()).isNotNull();
        assertThat(fluentYatspec.testState().interestingGivens.isEmpty());
        assertThat(fluentYatspec.testState().capturedInputAndOutputs.isEmpty());
    }

    class TestResult {

    }
}