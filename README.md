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
	<version>1.0.6</version>
</dependency>
```
