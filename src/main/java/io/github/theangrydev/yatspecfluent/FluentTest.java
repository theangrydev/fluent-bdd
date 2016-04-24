package io.github.theangrydev.yatspecfluent;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public abstract class FluentTest<Request, Response> implements WithTestState, ReadOnlyTestItems {

    private final InterestingGivens interestingGivens = new InterestingGivens();
    private final CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
    private final List<Given> givens = new ArrayList<>();

    private Stage stage = Stage.GIVEN;
    private When<Request, Response> when;
    private Response response;

    private enum Stage {
        GIVEN,
        WHEN,
        THEN
    }

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
        testState.interestingGivens = interestingGivens;
        testState.capturedInputAndOutputs = capturedInputAndOutputs;
        return testState;
    }

    protected <T extends Given> T and(T dependency) {
        return given(dependency);
    }

    protected <T extends Given> T given(T dependency) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("The 'given' steps must be specified before the 'when' and 'then' steps");

        }
        boolean alreadyHadGiven = givens.stream().map(Object::getClass).anyMatch(aClass -> aClass.equals(dependency.getClass()));
        if (alreadyHadGiven) {
            throw new IllegalStateException(format("The dependency '%s' has already specified a 'given' step", dependency));
        }
        if (!givens.isEmpty()) {
            primePreviousGiven();
        }
        givens.add(dependency);
        return dependency;
    }

    private void primePreviousGiven() {
        if (!givens.isEmpty()) {
            givens.get(givens.size() - 1).prime();
        }
    }

    protected <T extends When<Request, Response>> T when(T when) {
        this.when = when;
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("There should only be one 'when', after the 'given' and before the 'then'");
        }
        if (!givens.isEmpty()) {
            primePreviousGiven();
        }
        stage = Stage.WHEN;
        return when;
    }

    protected <Then> Then then(ThenFactory<Then, Response> thenFactory) {
        if (stage == Stage.GIVEN) {
            throw new IllegalStateException("The initial 'then' should be after the 'when'");
        }
        if (stage == Stage.THEN) {
            throw new IllegalStateException("After the first 'then' you should use 'and'");
        }
        Request request = when.request();
        if (request == null) {
            throw new IllegalStateException(format("%s request was null", when));
        }
        response = when.response(request);
        if (response == null) {
            throw new IllegalStateException(format("%s response was null", when));
        }
        stage = Stage.THEN;
        return thenFactory.then(response);
    }

    protected <Then> Then and(ThenFactory<Then, Response> thenFactory) {
        if (stage != Stage.THEN) {
            throw new IllegalStateException("All of the 'then' statements after the initial then should be 'and'");
        }
        return thenFactory.then(response);
    }

    @Override
    public void addToGivens(String key, Object instance) {
        interestingGivens.add(key, instance);
    }

    @Override
    public void addToCapturedInputsAndOutputs(String key, Object instance) {
        capturedInputAndOutputs.add(key, instance);
    }
}
