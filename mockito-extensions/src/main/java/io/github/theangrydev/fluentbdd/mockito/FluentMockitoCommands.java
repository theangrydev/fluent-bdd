package io.github.theangrydev.fluentbdd.mockito;

import io.github.theangrydev.fluentbdd.core.WithFluentBdd;

public interface FluentMockitoCommands<TestClass> extends WithFluentBdd<TestClass> {
    <Mock> FluentMockitoGiven<Mock> given(Mock mock);
    default <Mock> FluentMockitoGiven<Mock> and(Mock mock) {
        return given(mock);
    }

    <Mock> Mock thenVerify(Mock mock);
    default <Mock> Mock andThenVerify(Mock mock) {
        return thenVerify(mock);
    }
}
