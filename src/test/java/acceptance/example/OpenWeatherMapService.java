package acceptance.example;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class OpenWeatherMapService {

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final HttpUrl weatherServiceUrl;

    public OpenWeatherMapService(String weatherServiceUrl) {
        this.weatherServiceUrl = HttpUrl.parse(weatherServiceUrl);
    }

    public String fetchWeatherFor(String city) {
        HttpUrl url = weatherServiceUrl.newBuilder().addPathSegment("data").addPathSegment("2.5").addPathSegment("weather").setQueryParameter("q", city).build();
        Response response = tryToFetchWeather(url);
        return parseDescription(responseBody(response));
    }

    private String parseDescription(String responseBody) {
        return new JSONObject(responseBody).getJSONArray("weather").getJSONObject(0).getString("description");
    }

    private String responseBody(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch body from response: " + response);
        }
    }

    private Response tryToFetchWeather(HttpUrl url) {
        try {
            return okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
        } catch (IOException ioException) {
            throw new RuntimeException("Could not fetch weather from: " + url, ioException);
        }
    }
}
