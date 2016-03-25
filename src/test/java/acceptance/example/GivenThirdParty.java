package acceptance.example;

import io.github.theangrydev.yatspecfluent.Dependency;
import io.github.theangrydev.yatspecfluent.InterestingGivensRecorder;

public class GivenThirdParty implements Dependency<TestInfrastructure> {
    private String returnValue;
    private String argument;

    public GivenThirdParty willReturn(String returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public GivenThirdParty whenGiven(String argument) {
        this.argument = argument;
        return this;
    }

    @Override
    public void prime(InterestingGivensRecorder interestingGivensRecorder, TestInfrastructure testInfrastructure) {
        testInfrastructure.wireMock();
    }
}
