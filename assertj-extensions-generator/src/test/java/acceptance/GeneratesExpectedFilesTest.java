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

import io.github.theangrydev.fluentbdd.assertjgenerator.WithFluentAssertJGeneratorMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.theangrydev.fluentbdd.assertjgenerator.ExpectedFiles.EXPECTED_DELEGATE_WITH_ASSERTIONS_CONTENT;
import static io.github.theangrydev.fluentbdd.assertjgenerator.ExpectedFiles.EXPECTED_WITH_FLUENT_ASSERT_J_CONTENT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GeneratesExpectedFilesTest {

    private static final String OUTPUT_PACKAGE = "io.github.theangrydev.fluentbdd.assertj";

    @Test
    public void generatesExpectedFiles() throws MojoFailureException, MojoExecutionException, IOException {
        Path outputDirectory = Files.createTempDirectory("");
        WithFluentAssertJGeneratorMojo generatorMojo = new WithFluentAssertJGeneratorMojo();
        generatorMojo.setProject(mock(MavenProject.class));
        generatorMojo.setOutputDirectory(outputDirectory.toFile());
        generatorMojo.setOutputPackage(OUTPUT_PACKAGE);

        generatorMojo.execute();

        Path delegateWithAssertionsFile = packagePath(outputDirectory).resolve("DelegateWithAssertions.java");
        assertEquals(EXPECTED_DELEGATE_WITH_ASSERTIONS_CONTENT, toString(delegateWithAssertionsFile));

        Path withFluentAssertJFile = packagePath(outputDirectory).resolve("WithFluentAssertJ.java");
        assertEquals(EXPECTED_WITH_FLUENT_ASSERT_J_CONTENT, toString(withFluentAssertJFile));
    }

    private Path packagePath(Path outputDirectory) {
        return outputDirectory.resolve("io").resolve("github").resolve("theangrydev").resolve("fluentbdd").resolve("assertj");
    }

    private String toString(Path file) throws IOException {
        return new String(Files.readAllBytes(file), UTF_8).replace("\r\n", "\n");
    }
}
