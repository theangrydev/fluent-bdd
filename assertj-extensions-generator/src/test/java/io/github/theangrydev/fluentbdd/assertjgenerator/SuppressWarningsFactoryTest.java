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

import junit.framework.TestCase;
import nl.jqno.equalsverifier.EqualsVerifier;

import static io.github.theangrydev.fluentbdd.assertjgenerator.SuppressWarningsAnnotation.suppressWarnings;
import static org.assertj.core.api.Assertions.assertThat;

// TODO: https://github.com/theangrydev/fluent-bdd/issues/14 @Test is not working for some strange reason...
public class SuppressWarningsFactoryTest extends TestCase {

    public void testSuppressWarnings() {
        SuppressWarnings unused = suppressWarnings("unused");

        assertThat(unused.annotationType()).isEqualTo(SuppressWarnings.class);
        assertThat(unused.value()).containsExactly("unused");
    }

    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(SuppressWarningsAnnotation.class).verify();
    }
}