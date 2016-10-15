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

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.Rule;

/**
 * Use this as the base class for your acceptance tests.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
@SuppressWarnings("PMD.TooManyMethods") // This is part of the API design choice
public abstract class FluentTest<TestResult> implements WithTestState, WriteOnlyTestItems {

    private final TestState state = new TestState();

    private TestResult testResult;

    @Rule
    public final FluentTestVerification<TestResult> verification = new FluentTestVerification<>();

    /**
     * You should aim to never access the state directly, but you might need to (e.g. global shared state).
     * Call {@link #addToGivens(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     * Call {@link #addToCapturedInputsAndOutputs(String, Object)} when possible or make use of the {@link WriteOnlyTestItems} interface.
     */
    @Override
    public TestState testState() {
        return state;
    }

    /**
     * Same as {@link #given(Given)}.
     * <p>
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    public void and(Given given) {
        given(given);
    }

    /**
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    public void given(Given given) {
        verification.checkGivenIsAllowed(given);
        given.prime();
        verification.recordGiven(given);
    }

    /**
     * Invoke the system under test and store the {@link TestResult} ready for the assertions.
     *
     * @param when The system under test, which should be built up inside the brackets
     * @param <T>  The type of {@link When}
     */
    public <T extends When<TestResult>> void when(T when) {
        verification.checkWhenIsAllowed();
        testResult = when.execute();
        verification.recordWhen(when, testResult);
    }

    /**
     * Adapt the 'when' to a 'given'. This is a common pattern when e.g. calling an endpoint that changes some state in the database.
     * This is the equivalent of {@link #given(Given)}.
     *
     * @param when The 'when' to adapt to a 'given'
     */
    public void given(When<TestResult> when) {
        given((Given) when::execute);
    }

    /**
     * Same as {@link #given(When)}.
     * <p>
     * Adapt the 'when' to a 'given'. This is a common pattern when e.g. calling an endpoint that changes some state in the database.
     * This is the equivalent of {@link #given(Given)}.
     *
     * @param when The 'when' to adapt to a 'given'
     */
    public void and(When<TestResult> when) {
        given(when);
    }

    /**
     * Perform an assertion. Assertions should be chained outside the brackets.
     *
     * @param thenAssertion A {@link ThenAssertion} that will produce a {@link Then} given the stored {@link TestResult}
     * @param <Then>      The type of fluent assertions that will be performed
     * @return The fluent assertions instance
     */
    public <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        verification.checkThenAssertionIsAllowed(thenAssertion);
        return thenAssertion.then(testResult);
    }

    /**
     * Same as {@link #then(ThenAssertion)}.
     * <p>
     * Perform an assertion. Assertions should be chained outside the brackets.
     *
     * @param thenAssertion A {@link ThenAssertion} that will produce a {@link Then} given the stored {@link TestResult}
     * @param <Then>      The type of fluent assertions that will be performed
     * @return The fluent assertions instance
     */
    public <Then> Then and(ThenAssertion<Then, TestResult> thenAssertion) {
        return then(thenAssertion);
    }

    /**
     * Same as {@link #then(ThenVerification)}.
     * <p>
     * Perform a verification, which should be built up inside the brackets.
     *
     * @param thenVerification A {@link ThenVerification}, which should be built up inside the brackets
     */
    public void and(ThenVerification<TestResult> thenVerification) {
        then(thenVerification);
    }

    /**
     * Perform a verification, which should be built up inside the brackets.
     *
     * @param thenVerification A {@link ThenVerification}, which should be built up inside the brackets
     */
    public void then(ThenVerification<TestResult> thenVerification) {
        verification.checkThenVerificationIsAllowed(thenVerification);
        thenVerification.verify(testResult);
        verification.recordThenVerification(thenVerification);
    }

    @Override
    public void addToGivens(String key, Object instance) {
        testState().interestingGivens.add(key, instance);
    }

    @Override
    public void addToCapturedInputsAndOutputs(String key, Object instance) {
        testState().capturedInputAndOutputs.add(key, instance);
    }
}
