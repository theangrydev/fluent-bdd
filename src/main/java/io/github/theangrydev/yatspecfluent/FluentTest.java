package io.github.theangrydev.yatspecfluent;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public abstract class FluentTest<
        TestInfrastructure,
        SystemUnderTest extends io.github.theangrydev.yatspecfluent.SystemUnderTest<TestInfrastructure, Request, Response>,
        Request,
        Response,
        Assertions> implements WithTestState, WithInterestingGivens, WithCapturedInputsAndOutputs, InterestingTestItems {

    private enum Stage {
        GIVEN,
        WHEN,
        THEN
    }

    private Stage stage = Stage.GIVEN;
    private List<Primer<TestInfrastructure>> primers = new ArrayList<>();

    private SystemUnderTest systemUnderTest;
    private Assertions assertions;

    @Rule
    public TestWatcher makeSureThenIsUsed = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            if (stage != Stage.THEN ) {
                throw new IllegalStateException("Each test needs at least a 'when' and a 'then'");
            }
        }
    };

    @Override
    public TestState testState() {
        TestState testState = new TestState();
        testState.interestingGivens = interestingGivens();
        testState.capturedInputAndOutputs = capturedInputAndOutputs();
        return testState;
    }

    @SuppressWarnings("unused")
    protected <D extends Primer<TestInfrastructure>> D given(D dependency, String messageToDisplay) {
        return given(dependency);
    }

    @SuppressWarnings("unused")
    protected <D extends Primer<TestInfrastructure>> D and(D dependency, String messageToDisplay) {
        return given(dependency);
    }

    protected <D extends Primer<TestInfrastructure>> D and(D dependency) {
        return given(dependency);
    }

    protected <D extends Primer<TestInfrastructure>> D given(D dependency) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("The 'given' steps must be specified before the 'when' and 'then' steps");

        }
        boolean alreadyHadGiven = primers.stream().map(Primer::getClass).anyMatch(aClass -> aClass.equals(dependency.getClass()));
        if (alreadyHadGiven) {
            throw new IllegalStateException(format("The dependency '%s' has already specified a 'given' step", dependency.getClass().getSimpleName()));
        }
        if (!primers.isEmpty()) {
            primePreviousGiven();
        }
        primers.add(dependency);
        return dependency;
    }

    private void primePreviousGiven() {
        if (!primers.isEmpty()) {
            primers.get(primers.size() - 1).prime(this, testInfrastructure());
        }
    }

    @SuppressWarnings("unused")
    protected SystemUnderTest when(String messageToDisplay) {
        return when();
    }

    protected SystemUnderTest when() {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("There should only be one 'when', after the 'given' and before the 'then'");
        }
        if (!primers.isEmpty()) {
            primePreviousGiven();
        }
        this.systemUnderTest = systemUnderTest();
        stage = Stage.WHEN;
        return systemUnderTest;
    }

    @SuppressWarnings("unused")
    protected Assertions then(String messageToDisplay)  {
        return then();
    }

    protected Assertions then() {
        if (stage == Stage.GIVEN) {
            throw new IllegalStateException("The initial 'then' should be after the 'when'");
        }
        if (stage == Stage.THEN) {
            throw new IllegalStateException("After the first 'then' you should use 'and'");
        }
        Request request = requestToSystemUnderTest();
        if (request == null) {
            throw new IllegalStateException(format("%s request was null", systemUnderTest));
        }
        beforeSystemHasBeenCalled(request);

        Response response = callSystemUnderTest(request);
        if (response == null) {
            throw new IllegalStateException(format("%s response was null", systemUnderTest));
        }
        afterSystemHasBeenCalled(response);
        stage = Stage.THEN;
        assertions = responseAssertions(response);
        return assertions;
    }

    private Request requestToSystemUnderTest() {
        try {
            return systemUnderTest.request(testInfrastructure());
        } catch (Exception exception) {
            throw new RuntimeException(format("%s threw an exception when called", systemUnderTest), exception);
        }
    }

    private Response callSystemUnderTest(Request request) {
        try {
            return systemUnderTest.call(request, testInfrastructure());
        } catch (Exception exception) {
            throw new RuntimeException(format("%s threw an exception when called", systemUnderTest), exception);
        }
    }

    protected Assertions and() {
        if (stage != Stage.THEN) {
            throw new IllegalStateException("All of the 'then' statements after the initial then should be 'and'");
        }
        return assertions;
    }

    protected abstract SystemUnderTest systemUnderTest();
    protected abstract void beforeSystemHasBeenCalled(Request request);
    protected abstract void afterSystemHasBeenCalled(Response response);
    protected abstract Assertions responseAssertions(Response response);
    protected abstract TestInfrastructure testInfrastructure();

    @Override
    public void addToGivens(String key, Object instance) {
        interestingGivens().add(key, instance);
    }

    @Override
    public void addToCapturedInputsAndOutputs(String key, Object instance) {
        capturedInputAndOutputs().add(key, instance);
    }
}
