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
package acceptance.example.thens;

import acceptance.example.test.TestResult;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

public class ThenTheResponseHeaders implements WithAssertions {

    private final Response response;

    public ThenTheResponseHeaders(TestResult testResult) {
        this.response = testResult.response;
    }

    public ThenTheResponseHeaders contains(String headerName) {
        assertThat(response.headers().names()).contains(headerName);
        return this;
    }
}
