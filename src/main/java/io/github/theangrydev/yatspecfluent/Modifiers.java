package io.github.theangrydev.yatspecfluent;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

class Modifiers {
    public static boolean isConstant(int modifiers) {
        return isStatic(modifiers) && isFinal(modifiers);
    }
}
