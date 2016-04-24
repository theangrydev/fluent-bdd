package io.github.theangrydev.yatspecfluent;

@FunctionalInterface
public interface ThenFactory<Then, Response> {
    Then then(Response response);
}
