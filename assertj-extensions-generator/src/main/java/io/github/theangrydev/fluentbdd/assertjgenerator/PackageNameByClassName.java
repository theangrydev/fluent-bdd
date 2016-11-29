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
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public final class PackageNameByClassName {

    private final Map<String, String> packageNames;
    private final String defaultPackageName;

    private PackageNameByClassName(Map<String, String> packageNames, String defaultPackageName) {
        this.packageNames = packageNames;
        this.defaultPackageName = defaultPackageName;
    }

    public static PackageNameByClassName packageNameByClassName(CompilationUnit compilationUnit, String defaultPackageName) {
        Map<String, String> packageNames = compilationUnit.getImports().stream()
                .filter(importDeclaration -> !importDeclaration.isStatic())
                .collect(toMap(PackageNameByClassName::className, PackageNameByClassName::packageName));
        return new PackageNameByClassName(packageNames, defaultPackageName);
    }

    public String packageName(String className) {
        try {
            return Class.forName("java.lang." + className).getPackage().getName();
        } catch (ClassNotFoundException e) {
            return ofNullable(packageNames.get(className)).orElse(defaultPackageName);
        }
    }

    private static String className(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return nameParts[nameParts.length - 1];
    }

    private static String packageName(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return stream(nameParts).limit(nameParts.length - 1L).collect(joining("."));
    }

    private static String[] nameParts(ImportDeclaration importDeclaration) {
        String name = importDeclaration.getName().toString();
        return name.split("\\.");
    }
}
