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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Arrays.stream;

public class Verification<TestResult> {

    private enum Stage {
        GIVEN,
        WHEN,
        THEN
    }

    private Stage stage = Stage.GIVEN;

    private final Set<Object> usedInstances = new HashSet<>();

    public void recordGiven(Object given) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("The 'given' steps must be specified before the 'when' and 'then' steps");
        }
        checkMutableInstanceHasNotAlreadyBeenUsed(given);
        stage = Stage.GIVEN;
        usedInstances.add(given);
    }

    public TestResult recordWhen(When<TestResult> when) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("There should only be one 'when', after the 'given' and before the 'then'");
        }
        TestResult testResult = when.execute();
        if (testResult == null) {
            throw new IllegalStateException(format("'%s' test result was null", when));
        }
        stage = Stage.WHEN;
        return testResult;
    }

    public void recordThen(Object then) {
        if (stage.compareTo(Stage.WHEN) < 0) {
            throw new IllegalStateException("The 'then' steps should be after the 'when'");
        }
        stage = Stage.THEN;
        checkMutableInstanceHasNotAlreadyBeenUsed(then);
        usedInstances.add(then);
    }

    public void checkThenHasBeenUsed() {
        if (stage != Stage.THEN) {
            throw new IllegalStateException("Each test needs at least a 'when' and a 'then'");
        }
    }

    private boolean appearsToBeMutable(Class<?> aClass) {
        return stream(aClass.getDeclaredFields())
                .mapToInt(Field::getModifiers)
                .anyMatch(modifiers -> !isFinal(modifiers));
    }

    private void checkMutableInstanceHasNotAlreadyBeenUsed(Object instance) {
        if (appearsToBeMutable(instance.getClass()) && usedInstances.contains(instance)) {
            throw new IllegalStateException(format("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", instance.getClass().getSimpleName()));
        }
    }
}
