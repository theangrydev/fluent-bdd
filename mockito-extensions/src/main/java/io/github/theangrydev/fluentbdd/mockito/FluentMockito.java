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
import io.github.theangrydev.fluentbdd.core.When;
import io.github.theangrydev.fluentbdd.core.WithFluentBdd;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.HashSet;
import java.util.Set;

/**
 * Use this as a JUnit {@link Rule} alongside {@link WithFluentMockito}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public class FluentMockito<TestResult> implements MethodRule, FluentMockitoCommands<TestResult>, MockCreationListener {

    private final MockitoFramework mockitoFramework;
    private final MockitoRule mockitoRule;

    private final FluentBdd<TestResult> fluentBdd;

    private final Set<Object> mocks = new HashSet<>();

    private InOrder inOrder;

    /**
     * @param fluentBdd The JUnit {@link FluentBdd} {@link Rule} that this class will integrate with
     */
    public FluentMockito(FluentBdd<TestResult> fluentBdd) {
        this(Mockito.framework(), MockitoJUnit.rule(), fluentBdd);
    }

    // Only visible for testing to inject MockitoFramework and MockitoRule
    FluentMockito(MockitoFramework mockitoFramework, MockitoRule mockitoRule, FluentBdd<TestResult> fluentBdd) {
        this.mockitoRule = mockitoRule;
        this.fluentBdd = fluentBdd;
        this.mockitoFramework = mockitoFramework;
        mockitoFramework.addListener(this);
    }

    @Override
    public <Mock> FluentMockitoGiven<Mock> given(Mock mock) {
        FluentMockitoGiven<Mock> given = new FluentMockitoGiven<>(mock);
        fluentBdd.verification.recordGiven(given);
        return given;
    }

    @Override
    public <Mock> Mock thenVerify(Mock mock) {
        return fluentBdd().then(testResult -> {
            return verify(mock);
        });
    }

    private <Mock> Mock verify(Mock mock) {
        if (inOrder == null) {
            inOrder = BDDMockito.inOrder(mocks.toArray());
        }
        return inOrder.verify(mock);
    }

    @Override
    public FluentBdd<TestResult> fluentBdd() {
        return fluentBdd;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        try {
            return mockitoRule.apply(base, method, target);
        } finally {
            mockitoFramework.removeListener(this);
        }
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }
}
