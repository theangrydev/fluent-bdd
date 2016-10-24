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
package io.github.theangrydev.fluentbdd.core;

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class FluentBddMultipleTest {
    private interface SomeDependency extends Given {}
    private final SomeDependency someDependency = mock(SomeDependency.class);

    @Rule
    public FluentBdd<FluentBddMultipleTest> fluentBdd = new FluentBdd<>(this);

    private int intResult;
    private String stringResult;

    @Test
    public void whenCallingCanSetIntResult() {
        fluentBdd.given(someDependency);
        fluentBdd.whenCalling(() -> intResult = 42);
        fluentBdd.then(fluentBddMultipleTest -> {
            assertThat(intResult).isEqualTo(42);
        });
    }

    @Test
    public void whenCallingCanSetStringResult() {
        fluentBdd.given(someDependency);
        fluentBdd.whenCalling(() -> stringResult = "wow another one!");
        fluentBdd.then(fluentBddMultipleTest -> {
            assertThat(stringResult).isEqualTo("wow another one!");
        });
    }
}
