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
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import org.junit.Test;

import static com.github.javaparser.Range.UNKNOWN;
import static io.github.theangrydev.fluentbdd.assertjgenerator.PackageNameByClassName.packageNameByClassName;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PackageNameByClassNameTest {

    private static final String DEFAULT_PACKAGE_NAME = "default";

    @Test
    public void staticImportsAreFilteredOut() {
        ImportDeclaration staticImport = new ImportDeclaration(UNKNOWN, new NameExpr("static java.util.Collections.singletonList"), true, false);

        CompilationUnit compilationUnit = new CompilationUnit(new PackageDeclaration(new NameExpr("io.github.theangrydev.fluentbdd.assertjgenerator")), singletonList(staticImport), emptyList());

        assertThat(packageNameByClassName(compilationUnit, DEFAULT_PACKAGE_NAME).packageName("singletonList")).isEqualTo(DEFAULT_PACKAGE_NAME);
    }

    @Test
    public void canLookUpPackageNameByClassName() {
        ImportDeclaration staticImport = new ImportDeclaration(UNKNOWN, new NameExpr("org.assertj.core.api.filter.Filters"), false, false);

        CompilationUnit compilationUnit = new CompilationUnit(new PackageDeclaration(new NameExpr("io.github.theangrydev.fluentbdd.assertjgenerator")), singletonList(staticImport), emptyList());

        assertThat(packageNameByClassName(compilationUnit, DEFAULT_PACKAGE_NAME).packageName("Filters")).isEqualTo("org.assertj.core.api.filter");
    }

    @Test
    public void javaLangClasses() {
        CompilationUnit compilationUnit = new CompilationUnit(new PackageDeclaration(new NameExpr("io.github.theangrydev.fluentbdd.assertjgenerator")), emptyList(), emptyList());

        assertThat(packageNameByClassName(compilationUnit, DEFAULT_PACKAGE_NAME).packageName("Integer")).isEqualTo("java.lang");
    }
}