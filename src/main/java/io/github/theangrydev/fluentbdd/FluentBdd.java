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
package io.github.theangrydev.fluentbdd;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Use this as the base class for your acceptance tests.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
@SuppressWarnings("PMD.TooManyMethods") // This is part of the API design
public class FluentBdd<TestResult> extends TestWatcher implements FluentBddCommands<TestResult> {

    private final Verification<TestResult> verification = new Verification<>();

    private final TestState state = new TestState();

    private TestResult testResult;

    @Rule
    public final TestWatcher testWatcher = this;

    @Override
    protected void succeeded(Description description) {
        verification.checkThenHasBeenUsed();
    }

    /**
     * You should aim to never access the state directly, but you might need to (e.g. global shared state).
     * Call {@link #addToGivens(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     * Call {@link #addToCapturedInputsAndOutputs(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     */
    @Override
    public TestState testState() {
        return state;
    }

    @Override
    public void and(Given given) {
        given(given);
    }

    /**
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    @Override
    public void given(Given given) {
        verification.checkGivenIsAllowed(given);
        given.prime();
        verification.recordGiven(given);
    }

    @Override
    public <T extends When<TestResult>> void when(T when) {
        verification.checkWhenIsAllowed();
        testResult = when.execute();
        verification.recordWhen(when, testResult);
    }

    @Override
    public void given(When<TestResult> when) {
        given((Given) when::execute);
    }

    @Override
    public void and(When<TestResult> when) {
        given(when);
    }

    @Override
    public <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        verification.checkThenAssertionIsAllowed(thenAssertion);
        return thenAssertion.then(testResult);
    }

    @Override
    public <Then> Then and(ThenAssertion<Then, TestResult> thenAssertion) {
        return then(thenAssertion);
    }

    @Override
    public void and(ThenVerification<TestResult> thenVerification) {
        then(thenVerification);
    }

    @Override
    public void then(ThenVerification<TestResult> thenVerification) {
        verification.checkThenVerificationIsAllowed(thenVerification);
        thenVerification.verify(testResult);
        verification.recordThenVerification(thenVerification);
    }
}
