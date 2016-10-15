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

public class FluentTestTest extends FluentTest<FluentTestTest.TestResult> implements WithAssertions {
    private static final Statement SUCCESSFUL_STATEMENT = new Statement() {
        @Override
        public void evaluate() throws Throwable {
            // everything OK
        }
    };

    private final ThenAssertion<TestAssertions, TestResult> testAssertions = TestAssertions::new;
    private final MutableThenAssertion mutableThenAssertion = new MutableThenAssertion();
    private final ImmutableThenAssertion immutableThenAssertion = new ImmutableThenAssertion();
    private final SomeThenVerification someThenVerification = mock(SomeThenVerification.class);
    private final MutableThenVerification mutableThenVerification = new MutableThenVerification();
    private final ImmutableThenVerification immutableThenVerification = new ImmutableThenVerification();
    private final TestSystem testSystem = mock(TestSystem.class);
    private final TestSystem nullResponseTestSystem = mock(TestSystem.class);
    private final SomeDependency someDependency = mock(SomeDependency.class);
    private final SomeDependency someDependency2 = mock(SomeDependency.class);
    private final AnotherDependency anotherDependency = mock(AnotherDependency.class);
    private final ImmutableDependency immutableDependency = new ImmutableDependency();
    private final MutableDependency mutableDependency = new MutableDependency();
    private final TestResult testResult = new TestResult();

    private boolean given;
    private boolean when;
    private boolean then;

    private static class MutableThenAssertion implements ThenAssertion<TestAssertions, TestResult> {

        private int state;

        @Override
        public TestAssertions then(TestResult testResult) {
            System.out.println("state = " + state);
            return new TestAssertions(testResult);
        }

        public MutableThenAssertion withState(int state) {
            this.state = state;
            return this;
        }
    }

    private static class ImmutableThenAssertion implements ThenAssertion<TestAssertions, TestResult> {

        @Override
        public TestAssertions then(TestResult testResult) {
            return new TestAssertions(testResult);
        }
    }

    private static class TestAssertions {

        final TestResult testResult;

        TestAssertions(TestResult testResult) {
            this.testResult = testResult;
        }
    }

    private static class MutableThenVerification implements ThenVerification<TestResult> {

        private int state;

        @Override
        public void verify(TestResult testResult) {
            System.out.println("state = " + state);
        }

        public MutableThenVerification withState(int state) {
            this.state = state;
            return this;
        }
    }

    private static class ImmutableThenVerification implements ThenVerification<TestResult> {

        @Override
        public void verify(TestResult testResult) {

        }
    }

    static class TestResult {}
    private interface TestSystem extends When<TestResult> {}

    private interface SomeDependency extends Given {}

    private static class ImmutableDependency implements Given {

        @Override
        public void prime() {

        }
    }

    private static class MutableDependency implements Given {

        private int state;

        @Override
        public void prime() {
            System.out.println("state = " + state);
        }

        public MutableDependency withState(int state) {
            this.state = state;
            return this;
        }
    }

    private interface AnotherDependency extends Given {}

    private interface SomeThenVerification extends ThenVerification<TestResult> {}

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
        then(someThenVerification);
        verify(someThenVerification).verify(testResult);
    }

    @Test
    public void andThenVerificationBehavesTheSameAsThen() {
        given(someDependency);
        when(testSystem);
        and(someThenVerification);
        verify(someThenVerification).verify(testResult);
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
        assertThatThrownBy(() -> then(mutableThenVerification)).hasMessage("The 'then' steps should be after the 'when'");
    }

    @Test
    public void eachTestNeedsAtLeastAWhenAndAThen() {
        assertThatThrownBy(() -> verification.apply(SUCCESSFUL_STATEMENT, EMPTY).evaluate())
                .hasMessage("Each test needs at least a 'when' and a 'then'");
    }

    @Test
    public void immutableGivenInstanceCanBeUsedMoreThanOnce() {
        given(immutableDependency);
        and(immutableDependency);
    }

    @Test
    public void mutableGivenInstancesCanBeUsedOnce() {
        given(mutableDependency.withState(20));
    }

    @Test
    public void mutableGivenInstancesCannotBeUsedMoreThanOnce() {
        assertThatThrownBy(() -> {
            given(mutableDependency.withState(20));
            and(mutableDependency);
        }).hasMessage(format("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", mutableDependency.getClass().getSimpleName()));
    }

    @Test
    public void immutableThenAssertionInstancesCanBeUsedMoreThanOnce() {
        given(someDependency);
        when(testSystem);
        then(immutableThenAssertion);
        and(immutableThenAssertion);
    }

    @Test
    public void mutableThenAssertionInstancesCannotBeUsedMoreThanOnce() {
        given(someDependency);
        when(testSystem);
        assertThatThrownBy(() -> {
            then(mutableThenAssertion.withState(5));
            and(mutableThenAssertion);
        }).hasMessage("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", mutableThenAssertion.getClass().getSimpleName());
    }

    @Test
    public void mutableThenAssertionInstancesCanBeUsedOnce() {
        given(someDependency);
        when(testSystem);
        then(mutableThenAssertion.withState(5));
    }

    @Test
    public void immutableThenVerificationInstancesCanBeUsedMoreThanOnce() {
        given(someDependency);
        when(testSystem);
        then(immutableThenVerification);
        and(immutableThenVerification);
    }

    @Test
    public void mutableThenVerificationInstancesCannotBeUsedMoreThanOnce() {
        assertThatThrownBy(() -> {
            given(someDependency);
            when(testSystem);
            then(mutableThenVerification.withState(11));
            and(mutableThenVerification);
        }).hasMessage("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", mutableThenVerification.getClass().getSimpleName());
    }

    @Test
    public void mutableThenVerificationInstancesCanBeUsedOnce() {
        given(someDependency);
        when(testSystem);
        then(mutableThenVerification.withState(11));
    }
}
