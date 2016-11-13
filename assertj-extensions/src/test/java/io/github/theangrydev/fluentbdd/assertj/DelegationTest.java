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
package io.github.theangrydev.fluentbdd.assertj;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class DelegationTest implements WithFluentAssertJ<DelegationTest> {

    private final FluentBdd<DelegationTest> fluentBdd = mock(FluentBdd.class);

    @Override
    public FluentBdd<DelegationTest> fluentBdd() {
        return fluentBdd;
    }

    @Test
    public void nonThenMethodsJustDelegate() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        AtomicReference<Method> delegateMethod = new AtomicReference<>();
        WithAssertions delegate = (WithAssertions) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{WithAssertions.class}, (proxy, method, args) -> {
            delegateMethod.set(method);
            return null;
        });
        setDelegate(delegate);
        for (Method method : nonThenMethods()) {
            Object[] mockParameters = mockParameters(method);
            method.invoke(this, mockParameters);
            assertThat(delegateMethod.get().getName()).isEqualTo(method.getName());
        }
    }

    private void setDelegate(WithAssertions delegate) throws NoSuchFieldException, IllegalAccessException {
        Field delegateField = WithFluentAssertJ.class.getField("DELEGATE");
        makeModifiable(delegateField);
        delegateField.set(this, delegate);
    }

    private void makeModifiable(Field delegateField) throws NoSuchFieldException, IllegalAccessException {
        Field modifierField = delegateField.getClass().getDeclaredField("modifiers");
        int modifiers = delegateField.getModifiers();
        modifiers = modifiers & ~Modifier.FINAL;
        modifierField.setAccessible(true);
        modifierField.setInt(delegateField, modifiers);
    }

    private Object[] mockParameters(Method method) {
        return stream(method.getParameterTypes())
                .map(this::mockParameter)
                .toArray();
    }

    private Object mockParameter(Class<?> aClass) {
        if (aClass == Float.class || aClass == float.class) {
            return 1.0f;
        }
        if (aClass == Double.class || aClass == double.class) {
            return 1.0d;
        }
        if (aClass == Long.class || aClass == long.class) {
            return 1L;
        }
        if (aClass == Integer.class || aClass == int.class) {
            return 1;
        }
        if (aClass == boolean.class) {
            return true;
        }
        if (aClass == String.class) {
            return "";
        }
        if (aClass == Class.class) {
            return aClass;
        }
        if (aClass.isArray()) {
            Object parameter = mockParameter(aClass.getComponentType());
            Object array = Array.newInstance(aClass.getComponentType(), 1);
            Array.set(array, 0, parameter);
            return array;
        }
        if (aClass.isAssignableFrom(Iterable.class)) {
            return Collections.emptyList();
        }
        return mock(aClass);
    }

    private List<Method> nonThenMethods() {
        return stream(WithFluentAssertJ.class.getDeclaredMethods())
                .filter(method -> !isThenMethod(method) && !isAndMethod(method) && !isFailMethod(method))
                .collect(toList());
    }

    private boolean isThenMethod(Method method) {
        return method.getName().startsWith("then");
    }

    private boolean isAndMethod(Method method) {
        return method.getName().startsWith("and");
    }

    private boolean isFailMethod(Method method) {
        return method.getName().equals("fail");
    }
}
