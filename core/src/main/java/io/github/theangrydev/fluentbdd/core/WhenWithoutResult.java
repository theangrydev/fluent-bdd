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

/**
 * This class represents the system under test.
 *
 * It should act as a builder for use in {@link FluentBdd}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern#Java_example">The Builder Pattern</a>
 */
@FunctionalInterface
public interface WhenWithoutResult {

    /**
     * Execute the request on the system under test. Any result is assumed to have been set by e.g. setting a field on
     * the test class, which is possible when the {@link FluentBdd#FluentBdd(Object) FluentBdd(TestResult)} constructor is used.
     */
    void execute();
}