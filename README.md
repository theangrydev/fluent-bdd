![Fluent BDD logo](https://raw.githubusercontent.com/theangrydev/fluent-bdd/master/logo.png)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.theangrydev.fluentbdd/all.svg)](https://mvnrepository.com/artifact/io.github.theangrydev.fluentbdd/all)
[![Javadoc](http://javadoc-badge.appspot.com/io.github.theangrydev.fluentbdd/all.svg?label=javadoc)](http://javadoc-badge.appspot.com/io.github.theangrydev.fluentbdd/all)
[![Gitter](https://badges.gitter.im/fluent-bdd/Lobby.svg)](https://gitter.im/fluent-bdd/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[![Build Status](https://travis-ci.org/theangrydev/fluent-bdd.svg?branch=master)](https://travis-ci.org/theangrydev/fluent-bdd)
[![codecov](https://codecov.io/gh/theangrydev/fluent-bdd/branch/master/graph/badge.svg)](https://codecov.io/gh/theangrydev/fluent-bdd)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/93ca6effd928494a92b974ca585a9b2a)](https://www.codacy.com/app/liam-williams/fluent-bdd?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=theangrydev/fluent-bdd&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/d3718741-f398-41e0-a8d8-d2cb40c8a4d9)](https://codebeat.co/projects/github-com-theangrydev-fluent-bdd)

[![Quality Gate](https://sonarcloud.io/api/project_badges/quality_gate?project=io.github.theangrydev.fluentbdd:fluent-bdd)](https://sonarcloud.io/dashboard?id=io.github.theangrydev.fluentbdd:fluent-bdd)

[Example:](https://github.com/theangrydev/fluent-bdd/blob/master/yatspec-extensions/src/test/java/acceptance/ExampleTest.java)
```java
@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<TestResult> {

    private final GivenTheWeatherService theWeatherService = new GivenTheWeatherService(this, testInfrastructure);
    private final ThenTheAccessLogLinesContaining theAccessLogLines = new ThenTheAccessLogLinesContaining();
    private final ThenAssertion<ThenTheResponse, TestResult> theResponse = ThenTheResponse::new;
    private final ThenAssertion<ThenTheResponseHeaders, TestResult> theResponseHeaders = ThenTheResponseHeaders::new;
    private final WhenTheWeatherIsRequested theUser = new WhenTheWeatherIsRequested(testInfrastructure, "TheUser");
    private final ThenTheWeatherServiceWasCalled theWeatherServiceWasCalled = new ThenTheWeatherServiceWasCalled();

    @Test
    public void assertionTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theResponse).isEqualTo("There is light rain in London");
        and(theResponseHeaders).contains("Content-Length").contains("Date");
    }

    @Test
    public void assertionBuilderTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theAccessLogLines.containing("GET /weather")).hasSize(1);
    }

    @Test
    public void verificationTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theWeatherServiceWasCalled.withCity("London"));
    }
}
```

Dependency:
```xml
<dependency>
	<groupId>io.github.theangrydev.fluentbdd</groupId>
	<artifactId>all</artifactId>
	<version>8.2.2</version>
</dependency>
```
You can also depend on the modules `core`, `assertj-extensions`, `hamcrest-extensions`, `mockito-extensions` and `yatspec-extensions` separately if you don't need all of them.

## Module Stability
| Module              | Stability     | Comments |
| ------------------- | ------------- | -------- |
| core                | Stable        | Used by a small Agile team on a daily basis since March 2016, contains the bulk of the Fluent BDD framework |
| yatspec-extensions  | Stable        | Used by a small Agile team on a daily basis since March 2016, lightweight module that pulls in the [YatSpec](https://github.com/bodar/yatspec) dependency and provides some small integrations |
| assertj-extensions  | Experimental  | No real world users yet, added since [AssertJ](http://joel-costigliola.github.io/assertj/) has lots of useful assertion methods that could come in handy |
| hamcrest-extensions | Experimental  | No real world users yet, added since [Hamcrest](http://hamcrest.org/) is a popular framework that users might already be invested in |
| mockito-extensions  | Experimental  | No real world users yet, developed as a curiosity to see how integration with [Mockito](http://site.mockito.org/) could be possible |

## Releases
The versioning scheme follows [Semantic Versioning 2.0.0](http://semver.org/), to help you identify non backwards-compatible changes when you are upgrading.

### 8.2.2
* Minor release after some refactoring, no functional changes

### 8.2.1
* Improving the documentation of the `fluent-mockito` module

### 8.2.0
* [#11](https://github.com/theangrydev/fluent-bdd/issues/11) New module: `hamcrest-extensions` that acts as a bridge for using [Hamcrest](http://hamcrest.org/) matchers as `then` assertions

### 8.1.2
* Added some javadoc to the `all` module with links to javadoc of the other modules

### 8.0.0
* [#15](https://github.com/theangrydev/fluent-bdd/issues/15) Fixed by making `WithFluentAssertJ<TestResult>` extend `WithFluentBdd<TestResult>` and calling `recordThen` in each `then` and `and` method. This change is not backwards compatible
 
### 7.2.2
* Updated transitive dependency versions
* Marked the transitive dependency `yatspec-zohhak-plugin` as `test` scope not `compile` scope

### 7.2.1
* New module: `assertj-extensions` delegates to the `WithAssertions` [AssertJ](http://joel-costigliola.github.io/assertj/) convenience methods
* New module: `assertj-extensions-generator`, that produces a plugin that is used to generate the source code for `assertj-extensions`

### 7.1.0
* Added an `all` convenience dependency that pulls in all the other modules

### 7.0.0
* Removed the concept of having a base class, instead encouraging using fields that are exposed via the `With*` interfaces
* New module: `mockito-extensions` that integrates with [Mockito](http://site.mockito.org/)
* New JUnit `@Rule` `FluentMockito` to use in conjunction with `FluentBdd`, along with `WithFluentMockito`
  
### 6.0.0
* [#8](https://github.com/theangrydev/fluent-bdd/issues/8) **Renamed the library to fluent-bdd**. This change is not backwards compatible
* The `groupId` is now `io.github.theangrydev.fluentbdd` and the `artifactId` is `core` and `yatspec-extensions`
* The packages were changed to reflect this, there is now `io.github.theangrydev.fluentbdd.core` and `io.github.theangrydev.fluentbdd.yatspec`
* The `YatspecFluent` class was renamed to `FluentYatspec`, similarly `WithYatspecFluent` was renamed to `WithFluentYatspec`
* There is a `FluentBdd` class that does not require yatspec at all, with a corresponding `WithFluentBdd` interface 

### 5.0.2
* [#7](https://github.com/theangrydev/fluent-bdd/issues/7) The functionality implemented in [#6](https://github.com/theangrydev/fluent-bdd/issues/6) turned out to be a bit too strict about what it considered to be "mutable". Now the definition of "mutable" is that all the fields must be final. This allows synthetic classes (e.g. a constructor reference) to go through, which turned out to be a common way to write ThenAssertion implementations

### 5.0.1
* Renamed the base class from `FluentTest` to `YatspecFluent`. This change is not backwards compatible
* Support for using `YatspecFluent` as a JUnit `@Rule` alongside a `WithYatspecFluent` interface for the BDD methods if you do not want to extend `YatspecFluent` as the base class for your tests   

### 4.2.0
* [#6](https://github.com/theangrydev/fluent-bdd/issues/6) Check if `ThenAssertion` instances have been reused. Also relaxed all the instance checks to classes that appear to be mutable (have at least one instance field)

### 4.1.1
* Minor changes to the validation messages

### 4.1.0
* [#3](https://github.com/theangrydev/fluent-bdd/issues/3) Check that the same `Given` instance is not used more than once. This is to make it harder to accidentally share state by using the same builder style instance more than once. Similarly for `ThenVerification`

### 4.0.1
* [#5](https://github.com/theangrydev/fluent-bdd/issues/5) There are now two kinds of `then` methods. `ThenAssertion` is used for chained assertions. `ThenVerification` is used for a blob verification that is built up. See the `ExampleTest` for example usage. This change is not backwards compatible since `Then` was renamed to `ThenAssertion`

### 3.0.1
* Made the `FluentTest` methods public. Protected was enough, but it's clearer that they are part of the public API if they are public
* Fixed some javadoc language issues

### 3.0.0
* Removed `InterestingGivens` and `CapturedInputAndOutputs` fields. The `TestState` can be accessed via the interface `WithTestState` if it is really needed. This change is not backwards compatible since the fields were visible to subclasses of `FluentTest`

### 2.0.1
* [#4](https://github.com/theangrydev/fluent-bdd/issues/4) Remove `Request` from `When`. This change is not backwards compatible
* Mark `Given` as a `@FunctionalInterface` since it has a single abstract method

### 1.6.0
* [#1](https://github.com/theangrydev/fluent-bdd/issues/1) Relax all the wording validation because it was just getting in the way for users rather than providing any value

### 1.5.0
* Adapt when to given
* Relax the constrains on when you must use `and`. Now you are free to use `and` and `given` and `and` and `then` interchangeably, so long as the first `given` is a `given` not an `and` and the first `then` is a `then` not an `and`

### 1.4.0
* Make it clear that the raw yatspec captured inputs and outputs and interesting givens should not be used

### 1.3.0
* Allow multiple givens of the same type, even if the given is the same instance (this is useful for e.g. priming a rest endpoint for multiple ids)

### 1.2.1
* Allow multiple givens of the same type, but not exactly the same one

### 1.2.0
* Lots of javadoc and renamed `ReadOnlyTestItems` to `WriteOnlyTestItems`

### 1.1.0
* Do not allow given given (should be given and)

### 1.0.7
* Make interesting givens and captured inputs and outputs protected because some yatspec plugins (e.g. sequence diagram generator) need to read them

### 1.0.6
* Updating yatspec to 1.23

### 1.0.0
* An alternative approach to givensbuilders etc in yatspec
