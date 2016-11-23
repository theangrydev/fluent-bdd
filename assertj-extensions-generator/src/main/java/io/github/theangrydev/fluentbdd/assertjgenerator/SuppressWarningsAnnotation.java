package io.github.theangrydev.fluentbdd.assertjgenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class SuppressWarningsAnnotation implements SuppressWarnings {

    private final String[] value;

    private SuppressWarningsAnnotation(String... value) {
        this.value = value;
    }

    public static SuppressWarnings suppressWarnings(final String... value) {
        return new SuppressWarningsAnnotation(value);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return SuppressWarnings.class;
    }

    @Override
    public String[] value() {
        return value.clone();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        SuppressWarningsAnnotation that = (SuppressWarningsAnnotation) other;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
