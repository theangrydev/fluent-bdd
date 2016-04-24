package acceptance.example.test;

import io.github.theangrydev.yatspecfluent.FluentTest;
import org.assertj.core.api.WithAssertions;

public abstract class AcceptanceTest<Request, Response> extends FluentTest<TestInfrastructure, Request, Response> implements WithAssertions {

    public AcceptanceTest() {
        super(new TestInfrastructure());
    }

    @Override
    protected void setUp(TestInfrastructure testInfrastructure) {
        testInfrastructure.setUp();
    }

    @Override
    protected void tearDown(TestInfrastructure testInfrastructure) {
        testInfrastructure.tearDown();
    }
}
