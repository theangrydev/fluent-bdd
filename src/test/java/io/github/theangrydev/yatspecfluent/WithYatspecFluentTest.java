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
import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class WithYatspecFluentTest implements WithYatspecFluent<WithYatspecFluentTest.TestResult>, WithAssertions {

    @Override
    public YatspecFluent<TestResult> yatspecFluent() {
        return yatspecFluent;
    }

    @Test
    public void applyDelegates() {
        Mockito.when(yatspecFluent.apply(base, description)).thenReturn(statement);

        assertThat(apply(base, description)).isEqualTo(statement);
    }

    @Test
    public void testStateDelegates() {
        Mockito.when(yatspecFluent.testState()).thenReturn(testState);

        assertThat(testState()).isEqualTo(testState);
    }

    @Test
    public void givenGivenDelegates() {
        given(given);

        Mockito.verify(yatspecFluent).given(given);
    }

    @Test
    public void andGivenDelegates() {
        and(given);

        Mockito.verify(yatspecFluent).and(given);
    }

    @Test
    public void whenDelegates() {
        when(when);

        Mockito.verify(yatspecFluent).when(when);
    }

    @Test
    public void givenWhenDelegates() {
        given(when);

        Mockito.verify(yatspecFluent).given(when);
    }

    @Test
    public void andWhenDelegates() {
        and(when);

        Mockito.verify(yatspecFluent).and(when);
    }

    @Test
    public void thenAssertionDelegates() {
        Mockito.when(yatspecFluent.then(thenAssertion)).thenReturn(then);

        assertThat(then(thenAssertion)).isEqualTo(then);
    }

    @Test
    public void andThenAssertionDelegates() {
        Mockito.when(yatspecFluent.and(thenAssertion)).thenReturn(then);

        assertThat(and(thenAssertion)).isEqualTo(then);
    }

    @Test
    public void thenVerificationDelegates() {
        then(thenVerification);

        Mockito.verify(yatspecFluent).then(thenVerification);
    }

    @Test
    public void andThenVerificationDelegates() {
        and(thenVerification);

        Mockito.verify(yatspecFluent).and(thenVerification);
    }

    @Mock
    private YatspecFluent<TestResult> yatspecFluent;

    @Mock
    private Statement statement;

    @Mock
    private Statement base;

    @Mock
    private Description description;

    @Mock
    private TestState testState;

    @Mock
    private Given given;

    @Mock
    private When<TestResult> when;

    @Mock
    private ThenAssertion<Then, TestResult> thenAssertion;

    @Mock
    private ThenVerification<TestResult> thenVerification;

    @Mock
    private Then then;

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    static class TestResult {

    }

    private static class Then {

    }
}