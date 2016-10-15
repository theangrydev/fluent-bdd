/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of yatspec-fluent.
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
package io.github.theangrydev.yatspecfluent;

import com.googlecode.yatspec.junit.ParameterResolverFactory;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.junit.TableRunner;
import io.github.theangrydev.yatspeczohhakplugin.ZohhakParameterResolver;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.theangrydev.yatspecfluent.Modifiers.isConstant;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.STATIC;

@RunWith(TableRunner.class)
public class ModifiersTest implements WithAssertions {

    static {
        ParameterResolverFactory.setParameterResolver(ZohhakParameterResolver.class);
    }

    @Table({
        @Row({"true", "true", "true"}),
        @Row({"true", "false", "false"}),
        @Row({"false", "true", "false"}),
        @Row({"false", "false", "false"})
    })
    @Test
    public void isConstantIfStaticAndFinal(boolean isStatic, boolean isFinal, boolean isConstant) {
        assertThat(isConstant(modifiers(isStatic, isFinal))).isEqualTo(isConstant);
    }

    private int modifiers(boolean isStatic, boolean isFinal) {
        int modifiers = 0;
        if (isStatic) {
            modifiers |= STATIC;
        }
        if (isFinal) {
            modifiers |= FINAL;
        }
        return modifiers;
    }
}