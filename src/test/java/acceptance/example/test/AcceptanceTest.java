package acceptance.example.test;

import io.github.theangrydev.yatspecfluent.FluentTest;
import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;

public abstract class AcceptanceTest<Request, Response> extends FluentTest<Request, Response> implements WithAssertions {

    protected final TestInfrastructure testInfrastructure = new TestInfrastructure();

    @Before
    public void setUp() {
        testInfrastructure.setUp();
    }

    @After
    public void tearDown() {
        testInfrastructure.tearDown();
    }
}
