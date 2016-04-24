package io.github.theangrydev.yatspecfluent;

public interface ReadOnlyTestItems {
    void addToGivens(String key, Object instance);
    void addToCapturedInputsAndOutputs(String key, Object instance);
}
