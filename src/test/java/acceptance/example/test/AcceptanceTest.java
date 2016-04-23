package acceptance.example.test;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import io.github.theangrydev.yatspecfluent.FluentTest;
import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;

import static java.lang.String.format;

public abstract class AcceptanceTest<
        SystemUnderTest extends io.github.theangrydev.yatspecfluent.SystemUnderTest<TestInfrastructure, Request,Response>,
        Request,
        Response,
        Assertions> extends FluentTest<TestInfrastructure, SystemUnderTest, Request, Response, Assertions> implements WithAssertions {

    private final TestInfrastructure testInfrastructure = new TestInfrastructure();
    private final InterestingGivens interestingGivens = new InterestingGivens();
    private final CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();

    private String caller;
    protected GivenOpenWeatherMap theWeatherService = new GivenOpenWeatherMap();

    @Override
    protected SystemUnderTest when(String caller) {
        this.caller = caller;
        return super.when(caller);
    }

    @Override
    protected SystemUnderTest when() {
        if (caller == null) {
            throw new IllegalStateException("The caller name must be specified in the 'when'");
        }
        return super.when();
    }

    @Override
    public InterestingGivens interestingGivens() {
        return interestingGivens;
    }

    @Override
    public CapturedInputAndOutputs capturedInputAndOutputs() {
        return capturedInputAndOutputs;
    }

    @Override
    protected TestInfrastructure testInfrastructure() {
        return testInfrastructure;
    }

    @Override
    protected void beforeSystemHasBeenCalled(Request request) {
        if (request == null) {
            throw new IllegalStateException(format("%s request was null", systemName()));
        }
        capturedInputAndOutputs.add(format("Request from %s to %s", caller, systemName()), request);
    }


    @Override
    protected void afterSystemHasBeenCalled(Response response) {
        if (response == null) {
            throw new IllegalStateException(format("%s response was null", systemName()));
        }
        capturedInputAndOutputs.add(format("Response from %s to %s", systemName(), caller), response);
    }

    private String systemName() {
        return "Example";
    }

    @Before
    public void setUp() {
        testInfrastructure.setUp();
    }

    @After
    public void tearDown() {
        testInfrastructure.tearDown();
    }
}
