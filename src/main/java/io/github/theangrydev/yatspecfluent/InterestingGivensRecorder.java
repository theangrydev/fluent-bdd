package io.github.theangrydev.yatspecfluent;

@FunctionalInterface
public interface InterestingGivensRecorder {
    void addToGivens(String key, Object instance);
}
