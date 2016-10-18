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
package acceptance.example.thens;

import org.assertj.core.api.WithAssertions;

import java.util.List;

public class ThenTheAccessLogLines implements WithAssertions {

    private final List<String> lines;

    public ThenTheAccessLogLines(List<String> lines) {
        this.lines = lines;
    }

    public ThenTheAccessLogLines hasSize(int size) {
        assertThat(lines).hasSize(size);
        return this;
    }
}