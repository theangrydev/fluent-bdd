package io.github.theangrydev.yatspecfluent;

public interface SystemUnderTest<TestInfrastructure, Result> {
    RequestResponse<Result> call(TestInfrastructure testInfrastructure);
}
