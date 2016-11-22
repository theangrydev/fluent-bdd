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

import com.github.javaparser.ast.type.*;
import com.squareup.javapoet.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

// TODO: https://github.com/theangrydev/fluent-bdd/issues/14 remove PMD suppression
@SuppressWarnings("PMD")
public class TypeNameDetermination {

    private final List<TypeVariableName> typeVariableNames;
    private final Map<String, String> packageNameByClassName;
    private final String defaultPackageName;

    public TypeNameDetermination(List<TypeVariableName> typeVariableNames, Map<String, String> packageNameByClassName, String defaultPackageName) {
        this.typeVariableNames = typeVariableNames;
        this.packageNameByClassName = packageNameByClassName;
        this.defaultPackageName = defaultPackageName;
    }

    public TypeName determineTypeName(Type type) {
        if (type instanceof VoidType) {
            return TypeName.VOID;
        }
        if (type instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) type;
            Optional<TypeVariableName> typeVariableName = typeVariableNames.stream()
                    .filter(name -> name.name.equals(classOrInterfaceType.getName()))
                    .findFirst();
            if (typeVariableName.isPresent()) {
                return typeVariableName.get();
            }
            ClassName rawType = ClassName.get(packageName(packageNameByClassName, classOrInterfaceType.getName()), classOrInterfaceType.getName());

            List<Type> typeArgs = classOrInterfaceType.getTypeArgs();
            if (typeArgs.isEmpty()) {
                return rawType;
            }

            TypeName[] typeNames = typeArgs.stream().map(this::determineTypeName).toArray(TypeName[]::new);
            return ParameterizedTypeName.get(rawType, typeNames);
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (wildcardType.getExtends() != null) {
                return WildcardTypeName.subtypeOf(determineTypeName(wildcardType.getExtends()));
            }
            if (wildcardType.getSuper() != null) {
                return WildcardTypeName.supertypeOf(determineTypeName(wildcardType.getSuper()));
            }
            return WildcardTypeName.subtypeOf(Object.class);
        }
        if (type instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) type;
            return primitiveTypeToTypeName(primitiveType);
        }
        if (type instanceof ReferenceType) {
            ReferenceType referenceType = (ReferenceType) type;
            if (referenceType.getArrayCount() == 0) {
                return determineTypeName(referenceType.getType());
            } else {
                return ArrayTypeName.of(determineTypeName(referenceType.getType()));
            }
        }
        throw new UnsupportedOperationException("Unsupported type: " + type);
    }

    private String packageName(Map<String, String> packageName, String name) {
        try {
            return Class.forName("java.lang." + name).getPackage().getName();
        } catch (ClassNotFoundException e) {
            return ofNullable(packageName.get(name)).orElse(defaultPackageName);
        }
    }

    private TypeName primitiveTypeToTypeName(PrimitiveType primitiveType) {
        switch (primitiveType.getType()) {
            case Boolean:
                return TypeName.BOOLEAN;
            case Char:
                return TypeName.CHAR;
            case Byte:
                return TypeName.BYTE;
            case Short:
                return TypeName.SHORT;
            case Int:
                return TypeName.INT;
            case Long:
                return TypeName.LONG;
            case Float:
                return TypeName.FLOAT;
            case Double:
                return TypeName.DOUBLE;
            default:
                throw new UnsupportedOperationException("Unknown type: " + primitiveType.getType());
        }
    }
}
