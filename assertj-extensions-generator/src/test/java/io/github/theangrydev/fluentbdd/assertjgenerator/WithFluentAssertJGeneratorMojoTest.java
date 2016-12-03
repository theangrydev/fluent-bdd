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
import org.junit.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class WithFluentAssertJGeneratorMojoTest {

    private static final JavaFile WITH_FLUENT_ASSERT_J = javaFile("WithFluentAssertJ");
    private static final JavaFile DELEGATE_WITH_ASSERTIONS = javaFile("DelegateWithAssertions");
    private static final String OUTPUT_PACKAGE = "io.github.theangrydev.fluentbdd.assertj";
    private static final Path OUTPUT_DIRECTORY = Paths.get("/some/directory");

    private final JavaFileReader javaFileReader = mock(JavaFileReader.class);
    private final JavaFileWriter javaFileWriter = mock(JavaFileWriter.class);
    private final JavaEmitter javaEmitter = mock(JavaEmitter.class);
    private final Log log = mock(Log.class);
    private final MavenProject mavenProject = mock(MavenProject.class);

    @Test
    public void testGeneratesWithFluentAssertJFile() throws MojoFailureException, MojoExecutionException, IOException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        generatorMojo.execute();

        verify(javaFileWriter).write(OUTPUT_DIRECTORY.toFile(), DELEGATE_WITH_ASSERTIONS);
        verify(javaFileWriter).write(OUTPUT_DIRECTORY.toFile(), WITH_FLUENT_ASSERT_J);
    }

    @Test
    public void outputDirectoryIsAddedAsAProjectCompileSourceRoot() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        generatorMojo.execute();

        verify(mavenProject).addCompileSourceRoot(OUTPUT_DIRECTORY.toFile().getAbsolutePath());
        InOrder inOrder = inOrder(log, mavenProject);
        inOrder.verify(log).info("Wrote " + packagePath(OUTPUT_DIRECTORY).resolve("DelegateWithAssertions.java"));
        inOrder.verify(log).info("Wrote " + packagePath(OUTPUT_DIRECTORY).resolve("WithFluentAssertJ.java"));
    }

    @Test
    public void infoLogging() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        generatorMojo.execute();

        verify(log).info("Wrote " + packagePath(OUTPUT_DIRECTORY).resolve("DelegateWithAssertions.java"));
        verify(log).info("Wrote " + packagePath(OUTPUT_DIRECTORY).resolve("WithFluentAssertJ.java"));
        verify(log, never()).debug(any(CharSequence.class));
    }

    @Test
    public void debugLoggingDisabled() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        when(log.isDebugEnabled()).thenReturn(false);

        generatorMojo.execute();

        verify(log, never()).debug(any(CharSequence.class));
    }

    @Test
    public void debugLoggingEnabled() throws MojoFailureException, MojoExecutionException, ParseException, ClassNotFoundException, IOException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        when(log.isDebugEnabled()).thenReturn(true);
        when(javaFileReader.fileContent(packagePath(OUTPUT_DIRECTORY).resolve("DelegateWithAssertions.java"))).thenReturn("DelegateWithAssertions content");
        when(javaFileReader.fileContent(packagePath(OUTPUT_DIRECTORY).resolve("WithFluentAssertJ.java"))).thenReturn("WithFluentAssertJ content");

        generatorMojo.execute();

        verify(log).debug("File content: DelegateWithAssertions content");
        verify(log).debug("File content: WithFluentAssertJ content");
    }

    @Test
    public void uncaughtExceptionsAreLogged() throws ParseException, ClassNotFoundException, MojoFailureException, MojoExecutionException {
        WithFluentAssertJGeneratorMojo generatorMojo = mojoWithMocks(OUTPUT_DIRECTORY);

        RuntimeException uncaughtException = new RuntimeException("boom");
        when(javaEmitter.withFluentAssertJ(any())).thenThrow(uncaughtException);

        generatorMojo.execute();

        verify(log).error("Problem generating", uncaughtException);
    }

    private WithFluentAssertJGeneratorMojo mojoWithMocks(Path outputDirectory) throws ParseException, ClassNotFoundException {
        WithFluentAssertJGeneratorMojo generatorMojo = new WithFluentAssertJGeneratorMojo(javaFileReader, javaFileWriter, javaEmitter);
        generatorMojo.setProject(mavenProject);
        generatorMojo.setOutputDirectory(outputDirectory.toFile());
        generatorMojo.setOutputPackage(OUTPUT_PACKAGE);
        generatorMojo.setLog(log);

        when(javaEmitter.delegateWithAssertions(OUTPUT_PACKAGE)).thenReturn(DELEGATE_WITH_ASSERTIONS);
        when(javaEmitter.withFluentAssertJ(OUTPUT_PACKAGE)).thenReturn(WITH_FLUENT_ASSERT_J);
        return generatorMojo;
    }

    private Path packagePath(Path outputDirectory) {
        return outputDirectory.resolve("io").resolve("github").resolve("theangrydev").resolve("fluentbdd").resolve("assertj");
    }

    private static JavaFile javaFile(String className) {
        TypeSpec typeSpec = TypeSpec.classBuilder(className).build();
        return JavaFile.builder(OUTPUT_PACKAGE, typeSpec).build();
    }
}