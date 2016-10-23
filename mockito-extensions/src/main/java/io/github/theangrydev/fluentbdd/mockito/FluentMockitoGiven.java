package io.github.theangrydev.fluentbdd.mockito;

import org.mockito.BDDMockito;

public class FluentMockitoGiven<Mock> {
    private final Mock mock;
    private Object result;

    public FluentMockitoGiven(Mock mock) {
        this.mock = mock;
    }

    public FluentMockitoGiven<Mock> willReturn(Object result) {
        this.result = result;
        return this;
    }

    public Mock when() {
        return BDDMockito.willReturn(result).given(mock);
    }
}
