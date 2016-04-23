package io.github.theangrydev.yatspecfluent;

public interface SystemUnderTest<TestInfrastructure, Request, Response> {
    Request request(TestInfrastructure testInfrastructure) throws Exception;
    Response call(Request request, TestInfrastructure testInfrastructure) throws Exception;
}
