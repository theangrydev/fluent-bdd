package io.github.theangrydev.yatspecfluent;

@FunctionalInterface
public interface CapturedInputsAndOutputsPopulator {
    void addToCapturedInputsAndOutputs(String key, Object instance);
}
