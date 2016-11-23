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
package io.github.theangrydev.fluentbdd.core;

/**
 * These are the BDD methods that are used to write tests.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
@SuppressWarnings({
    "PMD.TooManyMethods", // This is by design
    "overloads", // This is unfortunate, but seems like the lesser evil than having many method names
    "FunctionalInterfaceClash" // This is unfortunate, but seems like the lesser evil than having many method names
})
public interface FluentBddCommands<TestResult> {

    /**
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    void given(Given given);

    /**
     * Same as {@link #given(Given)}.
     * <p>
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    default void and(Given given) {
        given(given);
    }

    /**
     * Invoke the system under test and store the {@link TestResult} ready for the assertions.
     *
     * @param when The system under test, which should be built up inside the brackets
     * @param <T>  The type of {@link When}
     */
    <T extends When<TestResult>> void when(T when);

    /**
     * Invoke the system under test. The {@link TestResult} is assumed to have been passed in already in the
     * {@link FluentBdd} constructor, which is useful to use the test class instance as the {@link TestResult}.
     * <p>
     * That way, many {@link org.junit.Test} methods can have different results and store them in the test class.
     *
     * @param whenWithoutResult The system under test, which should be built up inside the brackets
     */
    default void whenCalling(WhenWithoutResult whenWithoutResult) {
        when(() -> {
            whenWithoutResult.execute();
            return theResult();
        });
    }

    /**
     * Fetch the result, if it has been computed yet.
     *
     * @return The {@link TestResult}.
     */
    TestResult theResult();

    /**
     * Adapt the 'when' to a 'given'. This is a common pattern when e.g. calling an endpoint that changes some state in the database.
     * This is the equivalent of {@link #given(Given)}.
     *
     * @param when The 'when' to adapt to a 'given'
     */
    default void given(When<TestResult> when) {
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
    default void and(When<TestResult> when) {
        given(when);
    }

    /**
     * Perform an assertion. Assertions should be chained outside the brackets.
     *
     * @param thenAssertion A {@link ThenAssertion} that will produce a {@link Then} given the stored {@link TestResult}
     * @param <Then>        The type of fluent assertions that will be performed
     * @return The fluent assertions instance
     */
    <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion);

    /**
     * Same as {@link #then(ThenAssertion)}.
     * <p>
     * Perform an assertion. Assertions should be chained outside the brackets.
     *
     * @param thenAssertion A {@link ThenAssertion} that will produce a {@link Then} given the stored {@link TestResult}
     * @param <Then>        The type of fluent assertions that will be performed
     * @return The fluent assertions instance
     */
    default <Then> Then and(ThenAssertion<Then, TestResult> thenAssertion) {
        return then(thenAssertion);
    }

    /**
     * Perform a verification, which should be built up inside the brackets.
     *
     * @param thenVerification A {@link ThenVerification}, which should be built up inside the brackets
     */
    void then(ThenVerification<TestResult> thenVerification);

    /**
     * Same as {@link #then(ThenVerification)}.
     * <p>
     * Perform a verification, which should be built up inside the brackets.
     *
     * @param thenVerification A {@link ThenVerification}, which should be built up inside the brackets
     */
    default void and(ThenVerification<TestResult> thenVerification) {
        then(thenVerification);
    }
}
