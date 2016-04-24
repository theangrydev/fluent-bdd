package acceptance;

import com.googlecode.yatspec.junit.SpecRunner;
import acceptance.example.test.AcceptanceTest;
import acceptance.example.test.WeatherApplicationUnderTest;
import acceptance.example.test.ResponseAssertions;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<WeatherApplicationUnderTest, Request, Response, ResponseAssertions> {

    public ExampleTest() {
        super(new WeatherApplicationUnderTest());
    }

    @Test
    public void callingGivenThenWhenThenThenThenAndIsAllowed() {
        given(theWeatherService, "OpenWeatherMap").willReturn().weatherDescription("light rain").forCity("London");
        when("the user").requestsTheWeather().forCity("London");
        then().theResponse().isEqualTo("There is light rain in London");
        and().theResponseHeaders().contains("Content-Length");
        and().theResponseHeaders().contains("Date");
    }

    @Override
    protected ResponseAssertions responseAssertions(Response response) {
        return new ResponseAssertions(response);
    }
}
