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
package io.github.theangrydev.fluentbdd.assertjgenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@SuppressWarnings("PMD.UseUtilityClass")
public class SuppressWarningsFactory {

    public static SuppressWarnings suppressWarnings(final String... value) {
        return new SuppressWarningsImplementation(value);
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class SuppressWarningsImplementation implements SuppressWarnings {

        private final String[] value;

        SuppressWarningsImplementation(String... value) {
            this.value = value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return SuppressWarnings.class;
        }

        @Override
        public String[] value() {
            return value.clone();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            SuppressWarningsImplementation that = (SuppressWarningsImplementation) other;
            return Arrays.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(value);
        }
    }
}
