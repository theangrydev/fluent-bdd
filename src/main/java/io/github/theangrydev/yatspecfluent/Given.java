package io.github.theangrydev.yatspecfluent;

public interface Given<TestInfrastructure> {
    void prime(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure);
}
