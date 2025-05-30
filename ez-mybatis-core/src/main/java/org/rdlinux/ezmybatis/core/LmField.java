package org.rdlinux.ezmybatis.core;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class LmField {
    public static <T, R> String of(SFunction<T, R> fn) {
        try {
            Method writeReplace = fn.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            Object serializedForm = writeReplace.invoke(fn);
            if (serializedForm instanceof SerializedLambda) {
                String methodName = ((SerializedLambda) serializedForm).getImplMethodName();
                return methodNameToFieldName(methodName);
            } else {
                throw new RuntimeException("serialized form is not a SerializedLambda");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String methodNameToFieldName(String methodName) {
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return deCapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            return deCapitalize(methodName.substring(2));
        }
        throw new IllegalArgumentException("Not a getter method: " + methodName);
    }

    private static String deCapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
