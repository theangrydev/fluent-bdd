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
package acceptance.example.test;

import acceptance.example.production.WeatherApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import io.github.theangrydev.yatspecfluent.WriteOnlyTestItems;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;

public class TestInfrastructure {

    private static final String SYSTEM_NAME = "WeatherApplication";

    private final WriteOnlyTestItems writeOnlyTestItems;

    private WireMockServer wireMockServer;
    private WeatherApplication weatherApplication;

    public TestInfrastructure(WriteOnlyTestItems writeOnlyTestItems) {
        this.writeOnlyTestItems = writeOnlyTestItems;
    }

    public String serverBaseUrl() {
        return weatherApplication.baseUrl();
    }

    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().port(1235));
        wireMockServer.start();

        String wireMockServerUrl = format("http://localhost:%d", wireMockServer.port());
        weatherApplication = new WeatherApplication(1234, wireMockServerUrl);
        weatherApplication.start();
    }

    public void tearDown() {
        wireMockServer.shutdown();
        weatherApplication.stop();
    }

    public Response execute(Request request) {
        try {
            return new OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void givenThat(String dependencyName, MappingBuilder mappingBuilder) {
        wireMockServer.givenThat(mappingBuilder);
        wireMockServer.addMockServiceRequestListener((request, response) -> {
            if (mappingBuilder.build().getRequest().match(request).isExactMatch()) {
                recordOutgoingRequest(dependencyName, request);
                recordIncomingResponse(dependencyName, response);
            }
        });
    }

    public void recordIncomingRequest(String caller, Request request) {
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("%s from %s to %s", request.method(), caller, SYSTEM_NAME), request);
    }

    public void recordOutgoingResponse(String caller, Response response) {
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("%s from %s to %s", response.code(), SYSTEM_NAME, caller), response);
    }

    public void recordOutgoingRequest(String dependencyName, com.github.tomakehurst.wiremock.http.Request request) {
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("%s from %s to %s", request.getMethod().getName(), SYSTEM_NAME, dependencyName), request);
    }

    public void recordIncomingResponse(String dependencyName, com.github.tomakehurst.wiremock.http.Response response) {
        writeOnlyTestItems.addToCapturedInputsAndOutputs(format("%s from %s to %s", response.getStatus(), dependencyName, SYSTEM_NAME), response);
    }
}
