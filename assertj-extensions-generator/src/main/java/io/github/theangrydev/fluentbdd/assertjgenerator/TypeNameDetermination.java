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
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.squareup.javapoet.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public final class TypeNameDetermination extends GenericVisitorAdapter<TypeName, Type> {

    private final Map<String, TypeVariableName> typeVariableNames;
    private final PackageNameByClassName packageNameByClassName;

    private TypeNameDetermination(Map<String, TypeVariableName> typeVariableNames, PackageNameByClassName packageNameByClassName) {
        this.typeVariableNames = typeVariableNames;
        this.packageNameByClassName = packageNameByClassName;
    }

    public static TypeNameDetermination typeNameDetermination(List<TypeVariableName> typeVariableNames, PackageNameByClassName packageNameByClassName) {
        Map<String, TypeVariableName> typeVariableNameByName = typeVariableNames.stream()
                .collect(toMap(typeVariableName -> typeVariableName.name, identity()));
        return new TypeNameDetermination(typeVariableNameByName, packageNameByClassName);
    }

    public TypeName determineTypeName(Type type) {
        return type.accept(this, type);
    }

    @Override
    public TypeName visit(VoidType voidType, Type type) {
        return TypeName.VOID;
    }

    @Override
    public TypeName visit(ClassOrInterfaceType classOrInterfaceType, Type type) {
        Optional<TypeVariableName> typeVariableName = ofNullable(typeVariableNames.get(classOrInterfaceType.getName()));
        if (typeVariableName.isPresent()) {
            return typeVariableName.get();
        }
        String packageName = packageNameByClassName.packageName(classOrInterfaceType.getName());
        ClassName rawType = ClassName.get(packageName, classOrInterfaceType.getName());

        List<Type> typeArgs = classOrInterfaceType.getTypeArgs();
        if (typeArgs.isEmpty()) {
            return rawType;
        }
        TypeName[] typeNames = typeArgs.stream()
                .map(typeArg -> typeArg.accept(this, type))
                .toArray(TypeName[]::new);
        return ParameterizedTypeName.get(rawType, typeNames);
    }

    @Override
    public TypeName visit(WildcardType wildcardType, Type type) {
        if (wildcardType.getExtends() != null) {
            return WildcardTypeName.subtypeOf(determineTypeName(wildcardType.getExtends()));
        }
        if (wildcardType.getSuper() != null) {
            return WildcardTypeName.supertypeOf(determineTypeName(wildcardType.getSuper()));
        }
        return WildcardTypeName.subtypeOf(Object.class);
    }

    @Override
    public TypeName visit(ReferenceType referenceType, Type type) {
        if (referenceType.getArrayCount() == 0) {
            return determineTypeName(referenceType.getType());
        } else {
            return ArrayTypeName.of(determineTypeName(referenceType.getType()));
        }
    }

    @Override
    public TypeName visit(PrimitiveType primitiveType, Type type) {
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
