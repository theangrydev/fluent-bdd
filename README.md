[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.theangrydev/yatspec-fluent/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.theangrydev/yatspec-fluent)
[![Javadoc](http://javadoc-badge.appspot.com/io.github.theangrydev/yatspec-fluent.svg?label=javadoc)](http://javadoc-badge.appspot.com/io.github.theangrydev/yatspec-fluent)
[![Gitter](https://badges.gitter.im/yatspec-fluent/Lobby.svg)](https://gitter.im/yatspec-fluent/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[![Build Status](https://travis-ci.org/theangrydev/yatspec-fluent.svg?branch=master)](https://travis-ci.org/theangrydev/yatspec-fluent)
[![codecov](https://codecov.io/gh/theangrydev/yatspec-fluent/branch/master/graph/badge.svg)](https://codecov.io/gh/theangrydev/yatspec-fluent)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8027156014f84fa18f413bea6213bea8)](https://www.codacy.com/app/liam-williams/yatspec-fluent?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=theangrydev/yatspec-fluent&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/2727cf34-ac3c-4c1f-85f7-144b625303c1)](https://codebeat.co/projects/github-com-theangrydev-yatspec-fluent)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=io.github.theangrydev:yatspec-fluent)](https://sonarqube.com/dashboard/index/io.github.theangrydev:yatspec-fluent)

# yatspec-fluent
[Example:](https://github.com/theangrydev/yatspec-fluent/blob/master/src/test/java/acceptance/ExampleTest.java)
```java
@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<Response> {

    private final GivenOpenWeatherMap theWeatherService = new GivenOpenWeatherMap(this, testInfrastructure);
    private final ThenFactory<ThenTheResponse, Response> theResponse = ThenTheResponse::new;
    private final ThenFactory<ThenTheResponseHeaders, Response> theResponseHeaders = ThenTheResponseHeaders::new;
    private final WhenTheWeatherIsRequested theUser = new WhenTheWeatherIsRequested(testInfrastructure, "TheUser");

    @Test
    public void assertionTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theResponse).isEqualTo("There is light rain in London");
        and(theResponseHeaders).contains("Content-Length");
        and(theResponseHeaders).contains("Date");
    }
}
```

Dependency:
```xml
<dependency>
	<groupId>io.github.theangrydev</groupId>
	<artifactId>yatspec-fluent</artifactId>
	<version>3.0.1</version>
</dependency>
```

## Releases
### 3.0.1
* Made the `FluentTest` methods public. Protected was enough, but it's clearer that they are part of the public API if they are public
* Fixed some javadoc language issues

### 3.0.0
* Removed `InterestingGivens` and `CapturedInputAndOutputs` fields. The `TestState` can be accessed via the interface `WithTestState` if it is really needed. This is a breaking change since the fields were visible to subclasses of `FluentTest`

### 2.0.1
* [#4](https://github.com/theangrydev/yatspec-fluent/issues/4) Remove `Request` from `When`. This is a breaking change
* Mark `Given` as a `@FunctionalInterface` since it has a single abstract method

### 1.6.0
* [#1](https://github.com/theangrydev/yatspec-fluent/issues/1) Relax all the wording validation because it was just getting in the way for users rather than providing any value

### 1.5.0
* Adapt when to given
* Relax the constrains on when you must use 'and'. Now you are free to use 'and' and 'given' and 'and' and 'then' interchangeably, so long as the first 'given' is a 'given' not an 'and' and the first 'then' is a 'then' not an 'and'.

### 1.4.0
* Make it clear that the raw yatspec captured inputs and outputs and interesting givens should not be used

### 1.3.0
* Allow multiple givens of the same type, even if the given is the same instance (this is useful for e.g. priming a rest endpoint for multiple ids)

### 1.2.1
* Allow multiple givens of the same type, but not exactly the same one

### 1.2.0
* Lots of javadoc and renamed ReadOnlyTestItems to WriteOnlyTestItems

### 1.1.0
* Do not allow given given (should be given and)

### 1.0.7
* Make interesting givens and captured inputs and outputs protected because some yatspec plugins (e.g. sequence diagram generator) need to read them

### 1.0.6
* Updating yatspec to 1.23

### 1.0.0
* An alternative approach to givensbuilders etc in yatspec
