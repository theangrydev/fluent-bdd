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
package acceptance;

import acceptance.example.givens.GivenTheWeatherService;
import acceptance.example.test.AcceptanceTest;
import acceptance.example.test.TestResult;
import acceptance.example.thens.ThenTheWeatherServiceWasCalled;
import acceptance.example.thens.ThenTheResponse;
import acceptance.example.thens.ThenTheResponseHeaders;
import acceptance.example.whens.WhenTheWeatherIsRequested;
import com.googlecode.yatspec.junit.SpecRunner;
import io.github.theangrydev.yatspecfluent.ThenAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpecRunner.class)
public class ExampleTest extends AcceptanceTest<TestResult> {

    private final GivenTheWeatherService theWeatherService = new GivenTheWeatherService(this, testInfrastructure);
    private final ThenAssertion<ThenTheResponse, TestResult> theResponse = ThenTheResponse::new;
    private final ThenAssertion<ThenTheResponseHeaders, TestResult> theResponseHeaders = ThenTheResponseHeaders::new;
    private final WhenTheWeatherIsRequested theUser = new WhenTheWeatherIsRequested(testInfrastructure, "TheUser");
    private final ThenTheWeatherServiceWasCalled theWeatherServiceWasCalled = new ThenTheWeatherServiceWasCalled();

    @Test
    public void assertionTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theResponse).isEqualTo("There is light rain in London");
        and(theResponseHeaders).contains("Content-Length").contains("Date");
        and(theResponseHeaders).contains("Date");
    }

    @Test
    public void verificationTest() {
        given(theWeatherService.willReturn().weatherDescription("light rain").forCity("London"));
        when(theUser.requestsTheWeather().forCity("London"));
        then(theWeatherServiceWasCalled.withCity("London"));
    }
}
