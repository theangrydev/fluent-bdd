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
package acceptance.example.test;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import io.github.theangrydev.thinhttpclient.api.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * This is the complete result of a test, which includes a HTTP response and a way to verify outbound HTTP interactions.
 * If there was a database, there would be methods here exposing the database state.
 */
public class TestResult {

    private final TestInfrastructure testInfrastructure;
    public final Response response;

    public TestResult(TestInfrastructure testInfrastructure, Response response) {
        this.testInfrastructure = testInfrastructure;
        this.response = response;
    }

    public void verifyThat(RequestPatternBuilder requestPatternBuilder) {
        testInfrastructure.verifyThat(requestPatternBuilder);
    }

    public List<String> accessLogLinesContaining(String term) {
        return accessLogLines().stream().filter(line -> line.contains(term)).collect(toList());
    }

    private List<String> accessLogLines() {
        try {
            return Files.readAllLines(Paths.get("access.log"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
