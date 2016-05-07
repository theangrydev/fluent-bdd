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
package acceptance.example.whens;

import acceptance.example.test.TestInfrastructure;
import io.github.theangrydev.yatspecfluent.WriteOnlyTestItems;
import io.github.theangrydev.yatspecfluent.When;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

import static java.lang.String.format;


public class WhenTheWeatherIsRequested implements When<Request, Response>, WithAssertions {

    private final WriteOnlyTestItems writeOnlyTestItems;
    private final TestInfrastructure testInfrastructure;
    private final String caller;

    private String city;

    public WhenTheWeatherIsRequested(WriteOnlyTestItems writeOnlyTestItems, TestInfrastructure testInfrastructure, String caller) {
        this.writeOnlyTestItems = writeOnlyTestItems;
        this.testInfrastructure = testInfrastructure;
        this.caller = caller;
    }

    @Override
    public Request request() {
        Request request = weatherRequest(testInfrastructure.serverBaseUrl());
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("Request from %s to %s", caller, systemName()), request);
        return request;
    }

    @Override
    public Response response(Request request) {
        Response response = testInfrastructure.execute(request);
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("Response from %s to %s", systemName(), caller), response);
        return response;
    }

    private Request weatherRequest(String baseUrl) {
        HttpUrl weatherUrl = HttpUrl.parse(baseUrl).newBuilder().addPathSegment("weather").addQueryParameter("city", this.city).build();
        return new Request.Builder().url(weatherUrl).build();
    }

    public WhenTheWeatherIsRequested forCity(String city) {
        this.city = city;
        return this;
    }

    public WhenTheWeatherIsRequested requestsTheWeather() {
        return this;
    }

    private String systemName() {
        return "Weather Application";
    }
}
