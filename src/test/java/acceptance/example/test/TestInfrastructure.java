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
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import io.github.theangrydev.yatspecfluent.WriteOnlyTestItems;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TestInfrastructure {

    private static final String SYSTEM_NAME = "WeatherApplication";
    private static final WireMockServer WIREMOCK;
    private static final RequestListenerHolder REQUEST_LISTENER_HOLDER = new RequestListenerHolder();

    private final WriteOnlyTestItems writeOnlyTestItems;

    private WeatherApplication weatherApplication;

    private List<InteractionToListenFor> interactionsToListenFor = new ArrayList<>();

    static {
        WIREMOCK = new WireMockServer(wireMockConfig().port(1235));
        WIREMOCK.addMockServiceRequestListener(REQUEST_LISTENER_HOLDER);
        WIREMOCK.start();
    }

    public TestInfrastructure(WriteOnlyTestItems writeOnlyTestItems) {
        this.writeOnlyTestItems = writeOnlyTestItems;
    }

    public String serverBaseUrl() {
        return weatherApplication.baseUrl();
    }

    public void setUp() {
        WIREMOCK.resetAll();
        REQUEST_LISTENER_HOLDER.delegate = this::recordInteraction;

        String wireMockServerUrl = format("http://localhost:%d", WIREMOCK.port());
        weatherApplication = new WeatherApplication(1234, wireMockServerUrl);
        weatherApplication.start();
    }

    public void tearDown() {
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
        WIREMOCK.givenThat(mappingBuilder);
        interactionsToListenFor.add(new InteractionToListenFor(dependencyName, mappingBuilder));
    }

    private void recordInteraction(com.github.tomakehurst.wiremock.http.Request request, com.github.tomakehurst.wiremock.http.Response response) {
        List<InteractionToListenFor> matches = interactionsToListenFor.stream()
                .filter(interactionToListenFor -> interactionToListenFor.matches(request))
                .collect(toList());
        if (matches.isEmpty()) {
            throw new IllegalStateException(format("Found an interaction that was not listened for. Request: %s%n%nResponse: %s%n", request, response));
        }
        if (matches.size() > 1) {
            throw new IllegalStateException(format("Found an interaction that was listened for multiple times. Request: %s%n%nResponse: %s%n", request, response));
        }
        String dependencyName = matches.get(0).dependencyName;
        recordOutgoingRequest(dependencyName, request);
        recordIncomingResponse(dependencyName, response);
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

    public void verifyThat(RequestPatternBuilder requestPatternBuilder) {
        WIREMOCK.verify(1, requestPatternBuilder);
    }

    private static class RequestListenerHolder implements RequestListener {
        RequestListener delegate;

        @Override
        public void requestReceived(com.github.tomakehurst.wiremock.http.Request request, com.github.tomakehurst.wiremock.http.Response response) {
            delegate.requestReceived(request, response);
        }
    }

    private static class InteractionToListenFor {
        final String dependencyName;
        final MappingBuilder mappingBuilder;

        private InteractionToListenFor(String dependencyName, MappingBuilder mappingBuilder) {
            this.dependencyName = dependencyName;
            this.mappingBuilder = mappingBuilder;
        }

        boolean matches(com.github.tomakehurst.wiremock.http.Request request) {
            return mappingBuilder.build().getRequest().match(request).isExactMatch();
        }
    }
}
