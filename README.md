[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.theangrydev/yatspec-fluent/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.theangrydev/yatspec-fluent)
[![Build Status](https://travis-ci.org/theangrydev/yatspec-fluent.svg?branch=master)](https://travis-ci.org/theangrydev/yatspec-fluent)

# yatspec-fluent
[Example:](https://github.com/theangrydev/yatspec-fluent/blob/master/src/test/java/acceptance/ExampleTest.java)
```java
@Test
public void callingGivenThenWhenThenThenThenAndIsAllowed() {
    given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
    when(weatherApplication.requestsTheWeather().forCity("London"));
    then(theResponse).isEqualTo("There is light rain in London");
    and(theResponseHeaders).contains("Content-Length");
    and(theResponseHeaders).contains("Date");
}
```

Dependency:
```xml
<dependency>
	<groupId>io.github.theangrydev</groupId>
	<artifactId>yatspec-fluent</artifactId>
	<version>1.6.0</version>
</dependency>
```

## Releases

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
