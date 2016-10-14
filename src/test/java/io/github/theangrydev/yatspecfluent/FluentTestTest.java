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

import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;

import static java.lang.String.format;
import static org.junit.runner.Description.EMPTY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class FluentTestTest extends FluentTest<FluentTestTest.TestResult> implements WithAssertions {
    private static final Statement SUCCESSFUL_STATEMENT = new Statement() {
        @Override
        public void evaluate() throws Throwable {
            // everything OK
        }
    };

    private final ThenAssertion<TestAssertions, TestResult> testAssertions = TestAssertions::new;
    private final TestVerification testVerification = new TestVerification();
    private final TestSystem testSystem = mock(TestSystem.class);
    private final TestSystem nullResponseTestSystem = mock(TestSystem.class);
    private final SomeDependency someDependency = mock(SomeDependency.class);
    private final SomeDependency someDependency2 = mock(SomeDependency.class);
    private final AnotherDependency anotherDependency = mock(AnotherDependency.class);
    private final TestResult testResult = new TestResult();

    private boolean given;
    private boolean when;
    private boolean then;

    private static class TestAssertions {

        final TestResult testResult;

        TestAssertions(TestResult testResult) {
            this.testResult = testResult;
        }
    }

    private static class TestVerification implements ThenVerification<TestResult> {

        TestResult testResult;

        @Override
        public void verify(TestResult testResult) {
            this.testResult = testResult;
        }
    }

    static class TestResult {}
    private interface TestSystem extends When<TestResult> {}
    private interface SomeDependency extends Given {}
    private interface AnotherDependency extends Given {}

    @Before
    public void setUp() {
        Mockito.when(testSystem.execute()).thenReturn(testResult);
    }

    @After
    public void putTestInValidState() {
        if (!given && !when) {
            given(someDependency);
        }
        if (!when) {
            when(testSystem);
        }
        if (!then) {
            then(testAssertions);
        }
    }

    @Override
    public void given(Given given) {
        super.given(given);
        this.given = true;
    }

    @Override
    public <T extends When<TestResult>> void when(T when) {
        super.when(when);
        this.when = true;
    }

    @Override
    public <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        Then then = super.then(thenAssertion);
        this.then = true;
        return then;
    }

    @Test
    public void hasTestState() {
        assertThat(testState().interestingGivens).isNotNull();
        assertThat(testState().capturedInputAndOutputs).isNotNull();
    }

    @Test
    public void nullResponseIsReported() {
        assertThatThrownBy(() -> {
            when(nullResponseTestSystem);
        }).hasMessage(format("'%s' test result was null", nullResponseTestSystem));
    }

    @Test
    public void testResultIsPassedToTheAssertions() {
        given(someDependency);
        when(testSystem);
        TestAssertions then = then(testAssertions);
        assertThat(then.testResult).isSameAs(testResult);
    }

    @Test
    public void andThenAssertionBehavesTheSameAsThen() {
        given(someDependency);
        when(testSystem);
        TestAssertions then = and(testAssertions);
        assertThat(then.testResult).isSameAs(testResult);
    }

    @Test
    public void testResultIsPassedToTheVerification() {
        given(someDependency);
        when(testSystem);
        then(testVerification);
        assertThat(testVerification.testResult).isSameAs(testResult);
    }

    @Test
    public void andThenVerificationBehavesTheSameAsThen() {
        given(someDependency);
        when(testSystem);
        and(testVerification);
        assertThat(testVerification.testResult).isSameAs(testResult);
    }

    @Test
    public void firstGivenIsPrimedAfterGiven() {
        given(someDependency);
        verify(someDependency).prime();
    }

    @Test
    public void firstWhenAdaptedToGivenIsPrimedAfterGiven() {
        given(testSystem);
        verify(testSystem).execute();
    }

    @Test
    public void subsequentGivensArePrimedAfterThem() {
        given(someDependency);
        and(anotherDependency);
        verify(anotherDependency).prime();
    }

    @Test
    public void subsequentWhenAdaptedToGivensArePrimedAfterThem() {
        given(someDependency);
        and(testSystem);
        verify(testSystem).execute();
    }

    @Test
    public void whenIsPrimedAfterWhen() {
        given(someDependency);
        and(anotherDependency);
        when(testSystem);
        verify(testSystem).execute();
    }

    @Test
    public void multipleGivensOfTheSameTypeAreAllowed() {
        given(someDependency);
        verify(someDependency).prime();
        and(someDependency2);
        verify(someDependency2).prime();
    }

    @Test
    public void callingSameGivenTwiceIsAllowed() {
        given(someDependency);
        verify(someDependency).prime();
        and(someDependency);
        verify(someDependency, times(2)).prime();
    }

    @Test
    public void callingGivenAfterWhenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void multipleWhensAreNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            when(testSystem);
        }).hasMessage("There should only be one 'when', after the 'given' and before the 'then'");
    }

    @Test
    public void callingGivenAfterThenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when(testSystem);
            then(testAssertions);
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void callingThenAssertionBeforeWhenIsNotAllowed() {
        assertThatThrownBy(() -> then(testAssertions)).hasMessage("The 'then' steps should be after the 'when'");
    }

    @Test
    public void callingThenVerificationBeforeWhenIsNotAllowed() {
        assertThatThrownBy(() -> then(testVerification)).hasMessage("The 'then' steps should be after the 'when'");
    }

    @Test
    public void eachTestNeedsAtLeastAWhenAndAThen() {
        assertThatThrownBy(() -> makeSureThenIsUsed.apply(SUCCESSFUL_STATEMENT, EMPTY).evaluate())
                .hasMessage("Each test needs at least a 'when' and a 'then'");
    }
}
