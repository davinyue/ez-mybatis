package org.rdlinux.ezmybatis.core.utils;

public class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "object can not be null");
    }

    public static void isTrue(Boolean object, String message) {
        if (object == null || !object) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(Boolean object) {
        isTrue(object, "object must be true");
    }
}
