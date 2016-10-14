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

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import io.github.theangrydev.yatspecfluent.FluentTest;
import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;

import java.util.Collections;

import static com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator.getHeaderContentForModalWindows;

public abstract class AcceptanceTest<Request, Response> extends FluentTest<Request, Response> implements WithAssertions, WithCustomResultListeners {

    protected final TestInfrastructure testInfrastructure = new TestInfrastructure(this);

    @Before
    public void setUp() {
        testInfrastructure.setUp();
    }

    @After
    public void tearDown() {
        addSequenceDiagram();
        testInfrastructure.tearDown();
    }

    private void addSequenceDiagram() {
        Iterable<SequenceDiagramMessage> messages = new ByNamingConventionMessageProducer().messages(testState().capturedInputAndOutputs);
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();
        addToCapturedInputsAndOutputs("Sequence Diagram", sequenceDiagramGenerator.generateSequenceDiagram(messages));
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return Collections.singleton(new HtmlResultRenderer()
                .withCustomHeaderContent(getHeaderContentForModalWindows())
                .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
    }
}
