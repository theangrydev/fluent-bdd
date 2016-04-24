package io.github.theangrydev.yatspecfluent;

public interface When<TestInfrastructure, Request, Response> {
    Request request(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure);
    Response response(Request request, ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure);
}