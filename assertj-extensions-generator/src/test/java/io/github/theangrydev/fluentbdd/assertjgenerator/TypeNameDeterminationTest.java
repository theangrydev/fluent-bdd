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

import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

// TODO: https://github.com/theangrydev/fluent-bdd/issues/14 @Test is not working for some strange reason...
public class TypeNameDeterminationTest extends TestCase {

    private final TypeNameDetermination typeNameDetermination = new TypeNameDetermination(emptyList(), emptyMap(), "package");

    public void testUnsupportedType() {
        assertThatThrownBy(() -> typeNameDetermination.determineTypeName(mock(Type.class)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}