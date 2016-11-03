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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.*;
import com.squareup.javapoet.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static javax.lang.model.element.Modifier.*;

/**
 * @goal generate-sources
 * @phase generate-sources
 */
// TODO: remove PMD suppression
@SuppressWarnings({"JavaDoc", "WeakerAccess", "PMD"}) // These are the maven plugin conventions...
public class WithFluentAssertJGeneratorMojo extends AbstractMojo {

    private static final String ASSERTJ_API_PACKAGE = "org.assertj.core.api";
    private static final String WITH_ASSERTIONS = "WithAssertions";
    private static final String ASSERTJ_ASSERTIONS_JAVA_FILE = WITH_ASSERTIONS + ".java";
    private static final String ASSERTJ_ASSERTIONS_JAVA_FILE_INCLUDING_PACKAGE = ASSERTJ_API_PACKAGE + "." + ASSERTJ_ASSERTIONS_JAVA_FILE;
    private static final String ASSERTJ_ASSERTIONS_JAVA_FILE_RESOURCE_PATH = ASSERTJ_API_PACKAGE.replace('.', '/') + "/" + ASSERTJ_ASSERTIONS_JAVA_FILE;
    private static final String OUTPUT_CLASS_NAME = "WithFluentAssertJ";
    private static final String ASSERT_THAT_METHOD_PREFIX = "assertThat";
    private static final String THEN_METHOD_PREFIX = "then";
    private static final String AND_THEN_METHOD_PREFIX = "and";
    private static final String FLUENT_BDD = "fluent-bdd";
    private static final String MODIFICATION_DESCRIPTION = "" +
            "This file was generated by the assertj-extensions-generator module of " + FLUENT_BDD + " using the " + ASSERTJ_ASSERTIONS_JAVA_FILE_INCLUDING_PACKAGE + " source code.\n" +
            "The original documentation from " + ASSERTJ_ASSERTIONS_JAVA_FILE + " has been preserved.\n" +
            "The modifications involve renaming 'assertThat' methods to 'then' and 'and' to better match the language used in " + FLUENT_BDD + ".\n";
    public static final String DELEGATE_WITH_ASSERTIONS_CLASS_NAME = "DelegateWithAssertions";
    private static final String NEW_DELEGATE_WITH_ASSERTIONS = "new " + DELEGATE_WITH_ASSERTIONS_CLASS_NAME + "()";
    private static final String DELEGATE = "DELEGATE";
    private static final String DOLLAR_ESCAPE = "$$";
    private static final String SELF_JAVADOC_LINK = " #";
    private static final String SUPPRESS_WARNINGS_UNCHECKED = "@SuppressWarnings(\"unchecked\")";

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
            InputStream assertionsSource = getClass().getClassLoader().getResourceAsStream(ASSERTJ_ASSERTIONS_JAVA_FILE_RESOURCE_PATH);
            CompilationUnit compilationUnit = JavaParser.parse(assertionsSource);

            JavaFile delegateWithAssertions = JavaFile.builder(outputPackage, delegateWithAssertions())
                    .indent("\t")
                    .build();
            delegateWithAssertions.writeTo(outputDirectory);

            JavaFile withFluentAssertJ = JavaFile.builder(outputPackage, withFluentAssertJ(compilationUnit))
                    .indent("\t")
                    .skipJavaLangImports(true)
                    .addFileComment(javadoc(compilationUnit.getComment().getContent(), THEN_METHOD_PREFIX))
                    .build();
            withFluentAssertJ.writeTo(outputDirectory);

            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());

            getLog().info("Wrote " + outputFile(DELEGATE_WITH_ASSERTIONS_CLASS_NAME));
            getLog().info("Wrote " + outputFile(OUTPUT_CLASS_NAME));

            if (getLog().isDebugEnabled()) {
                logFile(DELEGATE_WITH_ASSERTIONS_CLASS_NAME);
                logFile(OUTPUT_CLASS_NAME);
            }
        } catch (Exception any) {
            getLog().error("Problem generating", any);
        }
    }

    private void logFile(String outputClassName) throws IOException {
        getLog().debug("File content: " + fileContent(outputClassName));
    }

    private String fileContent(String outputClassName) throws IOException {
        return new String(Files.readAllBytes(outputFile(outputClassName)), UTF_8);
    }

    private TypeSpec delegateWithAssertions() {
        return TypeSpec.classBuilder(DELEGATE_WITH_ASSERTIONS_CLASS_NAME)
                .addModifiers(PUBLIC)
                .addSuperinterface(ClassName.get(ASSERTJ_API_PACKAGE, WITH_ASSERTIONS))
                .build();
    }

    private Path outputFile(String outputClassName) {
        return outputDirectory.toPath()
                .resolve(outputPackage.replace('.', File.separatorChar))
                .resolve(outputClassName + ".java");
    }

    private TypeSpec withFluentAssertJ(CompilationUnit compilationUnit) throws ParseException, ClassNotFoundException {
        TypeDeclaration typeDeclaration = typeDeclaration(compilationUnit);

        Map<String, String> packageNameByClassName = compilationUnit.getImports().stream()
                .filter(importDeclaration -> !importDeclaration.isStatic())
                .collect(toMap(this::className, this::packageName));

        FieldSpec delegateField = FieldSpec.builder(
                ClassName.get(outputPackage, DELEGATE_WITH_ASSERTIONS_CLASS_NAME), DELEGATE, PUBLIC, STATIC, FINAL)
                .initializer(NEW_DELEGATE_WITH_ASSERTIONS)
                .build();

        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(OUTPUT_CLASS_NAME)
                .addAnnotation(AnnotationSpec.get(suppressWarnings("PMD")))
                .addModifiers(PUBLIC)
                .addField(delegateField)
                .addJavadoc(MODIFICATION_DESCRIPTION)
                .addJavadoc(javadoc(typeDeclaration.getJavaDoc().getContent(), THEN_METHOD_PREFIX));

        for (MethodDeclaration methodDeclaration : methodDeclarations(typeDeclaration)) {
            if (methodDeclaration.getName().startsWith(ASSERT_THAT_METHOD_PREFIX)) {
                builder.addMethod(methodSpec(packageNameByClassName, methodDeclaration, THEN_METHOD_PREFIX));
                builder.addMethod(methodSpec(packageNameByClassName, methodDeclaration, AND_THEN_METHOD_PREFIX));
            } else {
                builder.addMethod(methodSpec(packageNameByClassName, methodDeclaration, ""));
            }
        }
        return builder.build();
    }

    private String className(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return nameParts[nameParts.length - 1];
    }

    private String packageName(ImportDeclaration importDeclaration) {
        String[] nameParts = nameParts(importDeclaration);
        return stream(nameParts).limit(nameParts.length - 1).collect(joining("."));
    }

    private String[] nameParts(ImportDeclaration importDeclaration) {
        String name = importDeclaration.getName().toString();
        return name.split("\\.");
    }

    private MethodSpec methodSpec(Map<String, String> packageNames, MethodDeclaration methodDeclaration, String thenMethodPrefix) throws ClassNotFoundException {
        List<TypeVariableName> rawTypeVariableNames = methodDeclaration.getTypeParameters().stream()
                .map(typeParameter -> TypeVariableName.get(typeParameter.getName()))
                .collect(toList());

        List<TypeVariableName> boundTypeVariableNames = methodDeclaration.getTypeParameters().stream()
                .map(typeParameter -> typeVariableName(packageNames, rawTypeVariableNames, typeParameter))
                .collect(toList());

        TypeName returnTypeName = typeName(packageNames, boundTypeVariableNames, methodDeclaration.getType());
        String methodName = methodDeclaration.getName().replace(ASSERT_THAT_METHOD_PREFIX, thenMethodPrefix);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(PUBLIC, DEFAULT)
                .addCode(code(methodDeclaration))
                .addJavadoc(javadoc(methodDeclaration, thenMethodPrefix))
                .returns(returnTypeName);

        boundTypeVariableNames.forEach(builder::addTypeVariable);

        if (methodDeclaration.getParameters().stream().anyMatch(this::suppressWarningsUnchecked)) {
            builder.addAnnotation(AnnotationSpec.get(suppressWarnings("unchecked")));
        }

        for (Parameter parameter : methodDeclaration.getParameters()) {
            builder.addParameter(typeName(packageNames, boundTypeVariableNames, parameter.getType()), parameter.getName());
        }
        return builder.build();
    }

    private boolean suppressWarningsUnchecked(Parameter parameter) {
        return parameter.getAnnotations().stream().anyMatch(annotation -> annotation.toString().equals(SUPPRESS_WARNINGS_UNCHECKED));
    }

    private TypeVariableName typeVariableName(Map<String, String> packageName, List<TypeVariableName> typeVariableNames, TypeParameter typeParameter) {
        List<ClassOrInterfaceType> typeBounds = typeParameter.getTypeBound();
        TypeName[] typeNames = typeBounds.stream()
                .map(typeBound -> typeName(packageName, typeVariableNames, typeBound))
                .toArray(TypeName[]::new);

        return TypeVariableName.get(typeParameter.getName(), typeNames);
    }

    private String code(MethodDeclaration methodDeclaration) {
        String parameters = methodDeclaration.getParameters().stream().map(Parameter::getName).collect(joining(","));
        if (methodDeclaration.getType() instanceof VoidType) {
            return DELEGATE + "." + methodDeclaration.getName() + "(" + parameters + ");\n";
        } else {
            return "return " + DELEGATE + "." + methodDeclaration.getName() + "(" + parameters + ");\n";
        }
    }

    private String javadoc(MethodDeclaration methodDeclaration, String thenMethodPrefix) {
        JavadocComment javadocComment = methodDeclaration.getJavaDoc();
        if (javadocComment == null) {
            return "";
        }
        return javadoc(methodDeclaration.getJavaDoc().getContent(), thenMethodPrefix);
    }

    private String javadoc(String content, String thenMethodPrefix) {
        String[] lines = content.split("\n");
        String javadocWithoutStars = stream(lines).map(this::removeStars).collect(joining("\n"));
        return javadocWithoutStars
                .replaceFirst("\n", "")
                .replace("$", DOLLAR_ESCAPE)
                .replace(SELF_JAVADOC_LINK + ASSERT_THAT_METHOD_PREFIX, SELF_JAVADOC_LINK + thenMethodPrefix);
    }

    private String removeStars(String line) {
        return line.trim().replaceFirst("^\\*", "").trim();
    }

    private TypeName typeName(Map<String, String> packageName, List<TypeVariableName> typeVariableNames, Type type) {
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
            ClassName rawType = ClassName.get(packageName(packageName, classOrInterfaceType.getName()), classOrInterfaceType.getName());

            List<Type> typeArgs = classOrInterfaceType.getTypeArgs();
            if (typeArgs.isEmpty()) {
                return rawType;
            }

            TypeName[] typeNames = typeArgs.stream().map(typeArg -> typeName(packageName, typeVariableNames, typeArg)).toArray(TypeName[]::new);
            return ParameterizedTypeName.get(rawType, typeNames);
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (wildcardType.getExtends() != null) {
                return WildcardTypeName.subtypeOf(typeName(packageName, typeVariableNames, wildcardType.getExtends()));
            }
            if (wildcardType.getSuper() != null) {
                return WildcardTypeName.supertypeOf(typeName(packageName, typeVariableNames, wildcardType.getSuper()));
            }
            return WildcardTypeName.subtypeOf(Object.class);
        }
        if (type instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) type;
            return primitiveType(primitiveType);
        }
        if (type instanceof ReferenceType) {
            ReferenceType referenceType = (ReferenceType) type;
            if (referenceType.getArrayCount() == 0) {
                return typeName(packageName, typeVariableNames, referenceType.getType());
            } else {
                return ArrayTypeName.of(typeName(packageName, typeVariableNames, referenceType.getType()));
            }
        }
        throw new UnsupportedOperationException("Unsupported type: " + type);
    }

    private TypeName primitiveType(PrimitiveType primitiveType) {
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

    private String packageName(Map<String, String> packageName, String name) {
        try {
            return Class.forName("java.lang." + name).getPackage().getName();
        } catch (ClassNotFoundException e) {
            return ofNullable(packageName.get(name)).orElse(ASSERTJ_API_PACKAGE);
        }
    }

    private List<MethodDeclaration> methodDeclarations(TypeDeclaration typeDeclaration) throws ParseException {
        return typeDeclaration.getMembers().stream()
                .filter(MethodDeclaration.class::isInstance)
                .map(MethodDeclaration.class::cast)
                .collect(toList());
    }

    private TypeDeclaration typeDeclaration(CompilationUnit compilationUnit) {
        List<TypeDeclaration> types = compilationUnit.getTypes();
        if (types.size() != 1) {
            throw new UnsupportedOperationException("Expected " + ASSERTJ_ASSERTIONS_JAVA_FILE + " to have one type but found: " + types);
        }
        return types.get(0);
    }

    private static SuppressWarnings suppressWarnings(final String... value) {
        return new SuppressWarnings() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }

            @Override
            public String[] value() {
                return value;
            }
        };
    }
}