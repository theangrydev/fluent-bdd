/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of fluent-bdd.
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

import io.github.theangrydev.thinhttpclient.api.HttpClient;
import io.github.theangrydev.thinhttpclient.api.RequestBuilder;
import io.github.theangrydev.thinhttpclient.api.Response;
import org.json.JSONObject;

import java.io.IOException;

import static io.github.theangrydev.thinhttpclient.apache.ApacheHttpClient.apacheHttpClient;
import static io.github.theangrydev.thinhttpclient.api.Method.GET;

public class OpenWeatherMapService {

    private final HttpClient httpClient = apacheHttpClient();
    private final String weatherServiceUrl;

    public OpenWeatherMapService(String weatherServiceUrl) {
        this.weatherServiceUrl = weatherServiceUrl;
    }

    public String fetchWeatherFor(String city) {
        String url = weatherServiceUrl + "/data/2.5/weather?q=" + city;
        Response response = tryToFetchWeather(url);
        return parseDescription(response.body);
    }

    private String parseDescription(String responseBody) {
        return new JSONObject(responseBody).getJSONArray("weather").getJSONObject(0).getString("description");
    }

    private Response tryToFetchWeather(String url) {
        try {
            return httpClient.execute(new RequestBuilder().method(GET).url(url));
        } catch (IOException ioException) {
            throw new RuntimeException("Could not fetch weather from: " + url, ioException);
        }
    }
}
