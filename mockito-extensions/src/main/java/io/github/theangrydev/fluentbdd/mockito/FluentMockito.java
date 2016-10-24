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
import org.mockito.BDDMockito;
import org.mockito.InOrder;

import java.util.HashSet;
import java.util.Set;

public class FluentMockito<TestResult> implements FluentMockitoCommands<TestResult> {

    private final Set<Object> mocks = new HashSet<>();
    private final FluentBdd<TestResult> fluentBdd;

    private InOrder inOrder;

    public FluentMockito(FluentBdd<TestResult> fluentBdd) {
        this.fluentBdd = fluentBdd;
    }

    //TODO: talk to the verification here to make sure the FluentMockitoGiven is not reused
    @Override
    public <Mock> FluentMockitoGiven<Mock> given(Mock mock) {
        mocks.add(mock);
        return new FluentMockitoGiven<>(mock);
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
}
