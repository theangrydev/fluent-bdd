package io.github.theangrydev.yatspecfluent;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@SuppressWarnings("PMD.TooManyMethods") // Need lots of methods to make the interface fluent
public abstract class FluentTest<TestInfrastructure, Request, Response> implements WithTestState, ReadOnlyTestItems {

    private final InterestingGivens interestingGivens = new InterestingGivens();
    private final CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
    private final TestInfrastructure testInfrastructure;
    private final List<Given<TestInfrastructure>> givens = new ArrayList<>();

    private Stage stage = Stage.GIVEN;
    private When<TestInfrastructure, Request, Response> when;
    private Response response;

    protected FluentTest(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;
    }

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

    protected <D extends Given<TestInfrastructure>> D and(D dependency) {
        return given(dependency);
    }

    protected <D extends Given<TestInfrastructure>> D given(D dependency) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("The 'given' steps must be specified before the 'when' and 'then' steps");

        }
        boolean alreadyHadGiven = givens.stream().map(Object::getClass).anyMatch(aClass -> aClass.equals(dependency.getClass()));
        if (alreadyHadGiven) {
            throw new IllegalStateException(format("The dependency '%s' has already specified a 'given' step", dependency));
        }
        givens.add(dependency);
        return dependency;
    }

    private void primePreviousGiven() {
        if (!givens.isEmpty()) {
            givens.get(givens.size() - 1).prime(this, testInfrastructure);
        }
    }

    protected <T extends When<TestInfrastructure, Request, Response>> T when(T when) {
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
        Request request = when.request(this, testInfrastructure);
        if (request == null) {
            throw new IllegalStateException(format("%s request was null", when));
        }
        response = when.response(request, this, testInfrastructure);
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

    protected abstract void setUp(TestInfrastructure testInfrastructure);
    protected abstract void tearDown(TestInfrastructure testInfrastructure);

    @Before
    public void setUp() {
        setUp(testInfrastructure);
    }

    @After
    public void tearDown() {
        tearDown(testInfrastructure);
    }
}
