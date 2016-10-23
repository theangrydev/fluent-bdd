package io.github.theangrydev.fluentbdd.mockito;

public class StupidCode {

    private final Dependency dependency;

    public StupidCode(Dependency dependency) {
        this.dependency = dependency;
    }

    public void voidThingToTest() {
        dependency.someMethod("thing");
        dependency.anotherMethod("bing");
    }

    public int thingToTest() {
        dependency.someMethod("thing");
        return dependency.anotherMethod("bing");
    }
}
