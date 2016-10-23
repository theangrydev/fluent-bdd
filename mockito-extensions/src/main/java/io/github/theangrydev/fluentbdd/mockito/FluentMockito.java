package io.github.theangrydev.fluentbdd.mockito;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import org.mockito.BDDMockito;
import org.mockito.InOrder;

import java.util.HashSet;
import java.util.Set;

public class FluentMockito<TestResult> implements FluentMockitoCommands<TestResult> {

    private Set<Object> mocks = new HashSet<>();
    private InOrder inOrder;

    private final FluentBdd<TestResult> fluentBdd;

    public FluentMockito(FluentBdd<TestResult> fluentBdd) {
        this.fluentBdd = fluentBdd;
    }

    //TODO: talk to the verification here to make sure the FluentMockitoGiven is not reused
    @Override
    public <Mock> FluentMockitoGiven<Mock> given(Mock mock) {
        mocks.add(mock);
        return new FluentMockitoGiven<>(mock);
    }

    @Override
    public <Mock> Mock thenVerify(Mock mock) {
        if (inOrder == null) {
            inOrder = BDDMockito.inOrder(mocks.toArray());
        }
        return fluentBdd().then(testResult -> {
            return inOrder.verify(mock);
        });
    }

    @Override
    public FluentBdd<TestResult> fluentBdd() {
        return fluentBdd;
    }
}
