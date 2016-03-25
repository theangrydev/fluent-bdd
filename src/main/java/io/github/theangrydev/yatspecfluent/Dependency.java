package io.github.theangrydev.yatspecfluent;

public interface Dependency<TestInfrastructure> {
    void prime(InterestingGivensRecorder interestingGivensRecorder, TestInfrastructure testInfrastructure);
}
