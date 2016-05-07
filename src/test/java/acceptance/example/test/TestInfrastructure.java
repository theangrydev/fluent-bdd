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
import com.github.tomakehurst.wiremock.client.WireMock;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class TestInfrastructure {

    private WireMock wireMock;
    private WeatherApplication weatherApplication;

    public WireMock wireMock() {
        return wireMock;
    }

    public String serverBaseUrl() {
        return weatherApplication.baseUrl();
    }

    public void setUp() {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(1235));
        wireMockServer.start();
        wireMock = new WireMock(wireMockServer);

        String wireMockServerUrl = String.format("http://localhost:%d", wireMockServer.port());
        weatherApplication = new WeatherApplication(1234, wireMockServerUrl);
        weatherApplication.start();
    }

    public void tearDown() {
        wireMock.shutdown();
        weatherApplication.stop();
    }

    public Response execute(Request request) {
        try {
            return new OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
