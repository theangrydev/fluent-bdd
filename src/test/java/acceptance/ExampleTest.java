package acceptance;

import acceptance.example.givens.GivenOpenWeatherMap;
import acceptance.example.test.*;
import acceptance.example.thens.ThenTheResponse;
import acceptance.example.thens.ThenTheResponseHeaders;
import acceptance.example.whens.WhenTheWeatherIsRequested;
import com.googlecode.yatspec.junit.SpecRunner;
import io.github.theangrydev.yatspecfluent.ThenFactory;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<Request, Response> {

    private final GivenOpenWeatherMap theWeatherService = new GivenOpenWeatherMap(this, testInfrastructure);
    private final ThenFactory<ThenTheResponse, Response> theResponse = ThenTheResponse::new;
    private final ThenFactory<ThenTheResponseHeaders, Response> theResponseHeaders = ThenTheResponseHeaders::new;
    private final WhenTheWeatherIsRequested weatherApplication = new WhenTheWeatherIsRequested(this, testInfrastructure, "CBS");

    @Test
    public void callingGivenThenWhenThenThenThenAndIsAllowed() {
        given(theWeatherService).willReturn().weatherDescription("light rain").forCity("London");
        when(weatherApplication).requestsTheWeather().forCity("London");
        then(theResponse).isEqualTo("There is light rain in London");
        and(theResponseHeaders).contains("Content-Length");
        and(theResponseHeaders).contains("Date");
    }
}
