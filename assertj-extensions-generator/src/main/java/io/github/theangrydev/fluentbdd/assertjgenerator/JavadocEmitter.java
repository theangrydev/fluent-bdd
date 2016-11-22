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

import com.github.javaparser.ast.comments.JavadocComment;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class JavadocEmitter {
    private static final String DOLLAR_ESCAPE = "$$";
    private static final String SELF_JAVADOC_LINK = " #";

    private final String assertThatMethodPrefix;

    public JavadocEmitter(String assertThatMethodPrefix) {
        this.assertThatMethodPrefix = assertThatMethodPrefix;
    }

    public String javadoc(String thenMethodPrefix, JavadocComment javaDoc) {
        return javadoc(javaDoc.getContent(), thenMethodPrefix);
    }

    public String javadoc(String content, String thenMethodPrefix) {
        String[] lines = content.split("\n");
        String javadocWithoutStars = stream(lines).map(JavadocEmitter::removeStars).collect(joining("\n"));
        return javadocWithoutStars
                .replaceFirst("\n", "")
                .replace("$", DOLLAR_ESCAPE)
                .replace(SELF_JAVADOC_LINK + assertThatMethodPrefix, SELF_JAVADOC_LINK + thenMethodPrefix);
    }

    private static String removeStars(String line) {
        return line.trim().replaceFirst("^\\*", "").trim();
    }
}
