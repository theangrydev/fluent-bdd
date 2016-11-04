package io.github.theangrydev.assertj;

import io.github.theangrydev.fluentbdd.assertj.WithFluentAssertJ;
import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.Given;
import io.github.theangrydev.fluentbdd.core.ThenAssertion;
import io.github.theangrydev.fluentbdd.core.WithFluentBdd;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class WithFluentAssertJTest implements WithFluentAssertJ, WithFluentBdd<WithFluentAssertJTest> {

    private interface SomeDependency extends Given {}
    private final SomeDependency someDependency = mock(SomeDependency.class);

    @Rule
    public FluentBdd<WithFluentAssertJTest> fluentBdd = new FluentBdd<>(this);

    @Override
    public FluentBdd<WithFluentAssertJTest> fluentBdd() {
        return fluentBdd;
    }

    private int intResult;

    @Test
    public void whenCallingCanSetIntResult() {
        given(someDependency);
        whenCalling(() -> intResult = 42);
        then(theResult().intResult).isLessThan(43); //TODO: #15 make this move the verification state along
        then((ThenAssertion<AbstractIntegerAssert<?>, WithFluentAssertJTest>) test -> WithFluentAssertJTest.this.then(test.intResult));
    }
}
