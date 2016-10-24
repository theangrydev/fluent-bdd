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
package io.github.theangrydev.fluentbdd.mockito;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

//TODO: rewrite these tests, they are crap and just for coverage at the moment
public class FluentMockitoListenerTest implements WithAssertions {

    private final MockitoFramework mockitoFramework = mock(MockitoFramework.class);
    private final MockitoRule mockitoRule = mock(MockitoRule.class);

    private final Statement base = mock(Statement.class);
    private final FrameworkMethod method = mock(FrameworkMethod.class);
    private final Object target = new Object();

    private final FluentBdd<FluentMockitoListenerTest> fluentBdd = new FluentBdd<>(this);
    private final FluentMockito<FluentMockitoListenerTest> fluentMockito = new FluentMockito<>(mockitoFramework, mockitoRule, fluentBdd);

    @Test
    public void listenerIsAddedOnConstruction() {
        verify(mockitoFramework).addListener(fluentMockito);
    }

    @Test
    public void listenerIsRemovedAfterMockitoRuleIsInvoked() throws Throwable {
        fluentMockito.apply(base, method, target);

        InOrder inOrder = inOrder(mockitoRule, mockitoFramework);
        inOrder.verify(mockitoRule).apply(base, method, target);
        inOrder.verify(mockitoFramework).removeListener(fluentMockito);
    }

    @Test(expected = RuntimeException.class)
    public void listenerIsRemovedEvenIfMockitoRuleThrowsAnException() throws Throwable {
        Mockito.when(mockitoRule.apply(base, method, target)).thenThrow(new RuntimeException());
        try {
            fluentMockito.apply(base, method, target);
        } finally {
            verify(mockitoFramework).removeListener(fluentMockito);
        }
    }
}