package io.github.theangrydev.yatspecfluent;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class FluentTestTest extends FluentTest<FluentTestTest.TestInfrastructure, FluentTestTest.TestSystem, FluentTestTest.Response, FluentTestTest.TestAssertions> implements WithAssertions {

    private static final String INTERESTING_GIVENS_KEY = "interesting key";
    private static final String INTERESTING_GIVENS_VALUE = "interesting value";
    private static final String CAPTURED_INPUTS_AND_OUTPUTS_KEY = "captured key";
    private static final String CAPTURED_INPUTS_AND_OUTPUTS_VALUE = "captured value";

    private final Object request = new Object();
    private final Response response = new Response();
    private final TestSystem testSystem = new TestSystem();
    private final TestInfrastructure testInfrastructure = new TestInfrastructure();
    private final TestAssertions testAssertions = new TestAssertions();
    private final SomeDependency someDependency = new SomeDependency();
    private final InterestingGivens interestingGivens = new InterestingGivens();
    private final CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();

    class TestSystem implements SystemUnderTest<TestInfrastructure, Response> {

        private TestInfrastructure testInfrastructure;

        @Override
        public RequestResponse<Response> call(TestInfrastructure testInfrastructure) {
            this.testInfrastructure = testInfrastructure;
            return new RequestResponse<>(request, response);
        }
    }

    class TestAssertions {}
    class Response {}
    class TestInfrastructure {}

    private class SomeDependency implements Primer<TestInfrastructure> {
        private TestInfrastructure testInfrastructure;

        @Override
        public void prime(InterestingTestItems interestingTestItems, TestInfrastructure testInfrastructure) {
            this.testInfrastructure = testInfrastructure;
            interestingTestItems.addToGivens(INTERESTING_GIVENS_KEY, INTERESTING_GIVENS_VALUE);
            interestingTestItems.addToGivens(CAPTURED_INPUTS_AND_OUTPUTS_KEY, CAPTURED_INPUTS_AND_OUTPUTS_VALUE);
        }
    }

    @Test
    public void systemUnderTestIsCalledWithTestInfrastructure() {
        given(someDependency);
        when();
        then();
        assertThat(testSystem.testInfrastructure).isSameAs(testInfrastructure);
    }

    @Test
    public void dependencyPrimeIsCalledWithInterestingGivensPrimerAndTestInfrastructure() {
        given(someDependency);
        when();
        then();
        assertThat(someDependency.testInfrastructure).isSameAs(testInfrastructure);
        assertThat(interestingGivens.getType(INTERESTING_GIVENS_KEY, INTERESTING_GIVENS_VALUE.getClass())).isSameAs(INTERESTING_GIVENS_VALUE);
        assertThat(interestingGivens.getType(CAPTURED_INPUTS_AND_OUTPUTS_KEY, CAPTURED_INPUTS_AND_OUTPUTS_VALUE.getClass())).isSameAs(CAPTURED_INPUTS_AND_OUTPUTS_VALUE);
    }

    @Test
    public void callingGivenAfterWhenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when();
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
        then();
    }

    @Test
    public void callingGivenAfterThenIsNotAllowed() {
        assertThatThrownBy(() -> {
            when();
            then();
            given(someDependency);
        }).hasMessage("The 'given' steps must be specified before the 'when' and 'then' steps");
    }

    @Test
    public void callingThenBeforeWhenIsNotAllowed() {
        assertThatThrownBy(this::then).hasMessage("The initial 'then' should be after the 'when'");
        when();
        then();
    }

    @Test
    public void callingThenMoreThanOnceIsNotAllowed() {
        assertThatThrownBy(() -> {
            given(someDependency);
            when();
            then();
            then();
        }).hasMessage("After the first 'then' you should use 'and'");
    }

    @Override
    protected TestSystem systemUnderTest() {
        return testSystem;
    }

    @Override
    protected TestAssertions responseAssertions(Response response) {
        return testAssertions;
    }

    @Override
    protected TestInfrastructure testInfrastructure() {
        return testInfrastructure;
    }

    @Override
    protected void afterSystemHasBeenCalled(RequestResponse result) {
        assertThat(result.getRequest()).isSameAs(request);
        assertThat(result.getResponse()).isSameAs(response);
    }

    @Override
    public CapturedInputAndOutputs capturedInputAndOutputs() {
        return capturedInputAndOutputs;
    }

    @Override
    public InterestingGivens interestingGivens() {
        return interestingGivens;
    }
}
