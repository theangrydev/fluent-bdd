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

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.VoidType;

import static java.util.stream.Collectors.joining;

public class ThenMethodCodeEmitter {

    private final String delegateFieldName;

    public ThenMethodCodeEmitter(String delegateFieldName) {
        this.delegateFieldName = delegateFieldName;
    }

    public String code(MethodDeclaration methodDeclaration, String thenMethodPrefix) {
        String parameters = methodDeclaration.getParameters().stream().map(Parameter::getName).collect(joining(","));
        String callDelegateCode = callDelegateCode(methodDeclaration, parameters);
        if (thenMethodPrefix.isEmpty()) {
            return callDelegateCode;
        } else {
            return "fluentBdd().verification.recordThen(this);\n" + callDelegateCode;
        }
    }

    private String callDelegateCode(MethodDeclaration methodDeclaration, String parameters) {
        if (methodDeclaration.getType() instanceof VoidType) {
            return delegateFieldName + "." + methodDeclaration.getName() + "(" + parameters + ");\n";
        } else {
            return "return " + delegateFieldName + "." + methodDeclaration.getName() + "(" + parameters + ");\n";
        }
    }
}
