/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of yatspec-fluent.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package acceptance.example.production;

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
