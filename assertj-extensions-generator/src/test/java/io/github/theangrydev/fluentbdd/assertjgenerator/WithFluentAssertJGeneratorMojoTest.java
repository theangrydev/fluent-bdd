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
package io.github.theangrydev.fluentbdd.assertjgenerator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class WithFluentAssertJGeneratorMojoTest {

    @Test
    public void testGeneratesWithFluentAssertJFile() throws MojoFailureException, MojoExecutionException, IOException {
        Path tempDirectory = Files.createTempDirectory(getClass().getName());

        WithFluentAssertJGeneratorMojo generatorMojo = new WithFluentAssertJGeneratorMojo();
        generatorMojo.setProject(mock(MavenProject.class));
        generatorMojo.setOutputDirectory(tempDirectory.toFile());
        generatorMojo.setOutputPackage("io.github.theangrydev.fluentbdd.assertj");

        generatorMojo.execute();

        Path packagePath = tempDirectory.resolve("io").resolve("github").resolve("theangrydev").resolve("fluentbdd").resolve("assertj");

        Path delegateWithAssertionsFile = packagePath.resolve("DelegateWithAssertions.java");
        assertEquals(toString(Paths.get("./src/test/resources/DelegateWithAssertions.java")), toString(delegateWithAssertionsFile));

        Path withFluentAssertJFile = packagePath.resolve("WithFluentAssertJ.java");
        assertEquals(toString(Paths.get("./src/test/resources/WithFluentAssertJ.java")), toString(withFluentAssertJFile));
    }

    private String toString(Path file) throws IOException {
        return new String(Files.readAllBytes(file), UTF_8).replace("\r\n", "\n");
    }
}