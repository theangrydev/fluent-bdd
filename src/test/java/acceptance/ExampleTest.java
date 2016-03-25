package acceptance;

import com.googlecode.yatspec.junit.SpecRunner;
import acceptance.example.AcceptanceTest;
import acceptance.example.PacRequestSystem;
import acceptance.example.Response;
import acceptance.example.ResponseAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<PacRequestSystem, Response, ResponseAssertions> {

    @Test
    public void callingGivenThenWhenThenThenThenAndIsAllowed() {
        given(theWeatherService, "OpenWeatherMap").willReturn("foo").whenGiven("bar");
        when("another system").callsTheEndpoint().withArgument("bar");
        then().theResponse().isEqualTo("response");
        and().theResponseHeader().containsContentLength();
        and().theResponseHeader().containsContentLength();
    }

    @Override
    protected PacRequestSystem systemUnderTest() {
        return new PacRequestSystem();
    }

    @Override
    protected ResponseAssertions responseAssertions(Response response) {
        return new ResponseAssertions(response);
    }
}
