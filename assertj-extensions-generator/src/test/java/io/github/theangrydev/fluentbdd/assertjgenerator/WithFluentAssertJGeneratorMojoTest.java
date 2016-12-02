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

import com.github.javaparser.ParseException;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WithFluentAssertJGeneratorMojoTest {

    private static final String OUTPUT_PACKAGE = "io.github.theangrydev.fluentbdd.assertj";

    private final JavaEmitter javaEmitter = mock(JavaEmitter.class);
    private final Log log = mock(Log.class);
    private final MavenProject mavenProject = mock(MavenProject.class);

    @Rule
    public TemporaryFolder outputDirectory = new TemporaryFolder();

    @Test
    public void testGeneratesWithFluentAssertJFile() throws MojoFailureException, MojoExecutionException, IOException {
        WithFluentAssertJGeneratorMojo generatorMojo = new WithFluentAssertJGeneratorMojo();
        generatorMojo.setProject(mock(MavenProject.class));
        generatorMojo.setOutputDirectory(outputDirectory.getRoot());
        generatorMojo.setOutputPackage(OUTPUT_PACKAGE);

        generatorMojo.execute();

        Path delegateWithAssertionsFile = packagePath().resolve("DelegateWithAssertions.java");
        assertEquals(toString(Paths.get("./src/test/resources/DelegateWithAssertions.java")), toString(delegateWithAssertionsFile));

        Path withFluentAssertJFile = packagePath().resolve("WithFluentAssertJ.java");
        assertEquals(toString(Paths.get("./src/test/resources/WithFluentAssertJ.java")), toString(withFluentAssertJFile));
    }

    @Test
    public void outputDirectoryIsAddedAsAProjectCompileSourceRoot() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks();

        generatorMojo.execute();

        verify(mavenProject).addCompileSourceRoot(outputDirectory.getRoot().getAbsolutePath());
        InOrder inOrder = inOrder(log, mavenProject);
        inOrder.verify(log).info("Wrote " + packagePath().resolve("DelegateWithAssertions.java"));
        inOrder.verify(log).info("Wrote " + packagePath().resolve("WithFluentAssertJ.java"));
    }

    @Test
    public void infoLogging() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks();

        generatorMojo.execute();

        verify(log).info("Wrote " + packagePath().resolve("DelegateWithAssertions.java"));
        verify(log).info("Wrote " + packagePath().resolve("WithFluentAssertJ.java"));
        verify(log, never()).debug(any(CharSequence.class));
    }

    @Test
    public void debugLoggingDisabled() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks();

        when(log.isDebugEnabled()).thenReturn(false);

        generatorMojo.execute();

        verify(log, never()).debug(any(CharSequence.class));
    }

    @Test
    public void debugLoggingEnabled() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks();

        when(log.isDebugEnabled()).thenReturn(true);

        generatorMojo.execute();

        verify(log, times(2)).debug(startsWith("File content: package io.github.theangrydev.fluentbdd.assertj;"));
    }

    @Test
    public void uncaughtExceptionsAreLogged() throws ParseException, ClassNotFoundException, MojoFailureException, MojoExecutionException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks();

        RuntimeException uncaughtException = new RuntimeException("boom");
        when(javaEmitter.withFluentAssertJ(any())).thenThrow(uncaughtException);

        generatorMojo.execute();

        verify(log).error("Problem generating", uncaughtException);
    }

    private WithFluentAssertJGeneratorMojo mojoWithMocks() throws ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = new WithFluentAssertJGeneratorMojo(javaEmitter);
        generatorMojo.setProject(mavenProject);
        generatorMojo.setOutputDirectory(outputDirectory.getRoot());
        generatorMojo.setOutputPackage(OUTPUT_PACKAGE);
        generatorMojo.setLog(log);

        when(javaEmitter.delegateWithAssertions(OUTPUT_PACKAGE)).thenReturn(javaFile("DelegateWithAssertions"));
        when(javaEmitter.withFluentAssertJ(OUTPUT_PACKAGE)).thenReturn(javaFile("WithFluentAssertJ"));
        return generatorMojo;
    }

    private Path packagePath() {
        return outputDirectory.getRoot().toPath().resolve("io").resolve("github").resolve("theangrydev").resolve("fluentbdd").resolve("assertj");
    }

    private JavaFile javaFile(String className) {
        TypeSpec typeSpec = TypeSpec.classBuilder(className).build();
        return JavaFile.builder(OUTPUT_PACKAGE, typeSpec).build();
    }

    private String toString(Path file) throws IOException {
        return new String(Files.readAllBytes(file), UTF_8).replace("\r\n", "\n");
    }
}