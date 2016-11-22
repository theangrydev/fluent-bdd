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

import com.squareup.javapoet.JavaFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.theangrydev.fluentbdd.assertjgenerator.JavaEmitter.javaEmitter;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @goal generate-sources
 * @phase generate-sources
 */
@SuppressWarnings({
    "PMD.DefaultPackage", // Needed by the maven plugin convention
    "PMD.AvoidCatchingGenericException" // Needed to ensure uncaught exceptions are logged
})
public class WithFluentAssertJGeneratorMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * @parameter default-value="target/generated-sources/assertj-extensions-generator"
     * @required
     */
    File outputDirectory;

    /**
     * @parameter default-value="io.github.theangrydev.fluentbdd.assertj"
     * @required
     */
    String outputPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            JavaEmitter javaEmitter = javaEmitter();
            JavaFile delegateWithAssertions = javaEmitter.delegateWithAssertions(outputPackage);
            JavaFile withFluentAssertJ = javaEmitter.withFluentAssertJ(outputPackage);

            writeJavaFile(delegateWithAssertions);
            writeJavaFile(withFluentAssertJ);

            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        } catch (Exception any) {
            getLog().error("Problem generating", any);
        }
    }

    private void writeJavaFile(JavaFile javaFile) throws IOException {
        javaFile.writeTo(outputDirectory);
        getLog().info("Wrote " + outputFile(javaFile));
        if (getLog().isDebugEnabled()) {
            logFile(javaFile);
        }
    }

    private void logFile(JavaFile outputClassName) throws IOException {
        getLog().debug("File content: " + fileContent(outputClassName));
    }

    private String fileContent(JavaFile outputClassName) throws IOException {
        return new String(Files.readAllBytes(outputFile(outputClassName)), UTF_8);
    }

    private Path outputFile(JavaFile outputClassName) {
        return outputDirectory.toPath()
                .resolve(outputPackage.replace('.', File.separatorChar))
                .resolve(outputClassName.typeSpec.name + ".java");
    }
}
