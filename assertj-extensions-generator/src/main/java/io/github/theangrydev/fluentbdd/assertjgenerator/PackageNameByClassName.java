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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class PackageNameByClassName {

    public Map<String, String> packageNameByClassName(CompilationUnit compilationUnit) {
        return compilationUnit.getImports().stream()
                .filter(importDeclaration -> !importDeclaration.isStatic())
                .collect(toMap(this::className, this::packageName));
    }

    private String className(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return nameParts[nameParts.length - 1];
    }

    private String packageName(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return stream(nameParts).limit(nameParts.length - 1L).collect(joining("."));
    }

    private String[] nameParts(ImportDeclaration importDeclaration) {
        String name = importDeclaration.getName().toString();
        return name.split("\\.");
    }
}
