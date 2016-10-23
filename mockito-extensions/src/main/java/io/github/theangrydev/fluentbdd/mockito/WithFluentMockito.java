package io.github.theangrydev.fluentbdd.mockito;

public interface WithFluentMockito<TestClass> extends FluentMockitoCommands<TestClass> {
    FluentMockito<TestClass> fluentMockito();

    @Override
    default <Mock> FluentMockitoGiven<Mock> given(Mock mock) {
        return fluentMockito().given(mock);
    }

    @Override
    default <Mock> Mock thenVerify(Mock mock) {
        return fluentMockito().thenVerify(mock);
    }
}
