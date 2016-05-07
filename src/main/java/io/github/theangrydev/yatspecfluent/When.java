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
package io.github.theangrydev.yatspecfluent;

/**
 * This class represents the system under test.
 *
 * It should act as a builder for use in {@link FluentTest}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern#Java_example">The Builder Pattern</a>
 *
 * @param <Request> The type of request the system accepts
 * @param <Response> The type of response the system produces
 */
public interface When<Request, Response> {

    /**
     * @return The {@link Request} that will be passed to {@link #response(Request)}
     */
    Request request();

    /**
     * @param request The {@link Request} produced by {@link #request()}
     * @return The {@link Response} from the system under test after executing the request
     */
    Response response(Request request);
}