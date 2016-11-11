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
package acceptance;

import io.github.theangrydev.fluentbdd.Documentation;
import io.github.theangrydev.fluentbdd.assertj.WithFluentAssertJ;
import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.hamcrest.WithFluentHamcrest;
import io.github.theangrydev.fluentbdd.mockito.FluentMockito;
import io.github.theangrydev.fluentbdd.yatspec.FluentYatspec;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PullsInAllModulesTest {

    @Test
    public void pullsInAllModules() {
        assertNotNull(FluentBdd.class);
        assertNotNull(FluentMockito.class);
        assertNotNull(FluentYatspec.class);
        assertNotNull(WithFluentAssertJ.class);
        assertNotNull(WithFluentHamcrest.class);
        assertNotNull(Documentation.class);
    }
}
