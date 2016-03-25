package acceptance.example.test;

import io.github.theangrydev.yatspecfluent.RequestResponse;
import io.github.theangrydev.yatspecfluent.SystemUnderTest;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;


public class WeatherApplicationUnderTest implements SystemUnderTest<TestInfrastructure, Response>, WithAssertions {

    private String city;

    @Override
    public RequestResponse<Response> call(TestInfrastructure testInfrastructure) throws Exception {
        OkHttpClient httpClient = testInfrastructure.httpClient();
        Request request = weatherRequest(testInfrastructure.serverBaseUrl());
        Response response = httpClient.newCall(request).execute();
        return new RequestResponse<>(request, response);
    }

    private Request weatherRequest(String baseUrl) {
        HttpUrl weatherUrl = HttpUrl.parse(baseUrl).newBuilder().addPathSegment("weather").addQueryParameter("city", this.city).build();
        return new Request.Builder().url(weatherUrl).build();
    }

    public void forCity(String city) {
        this.city = city;
    }

    public WeatherApplicationUnderTest requestsTheWeather() {
        return this;
    }
}
