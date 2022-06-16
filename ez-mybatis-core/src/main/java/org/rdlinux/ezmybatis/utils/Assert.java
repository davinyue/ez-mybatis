package org.rdlinux.ezmybatis.utils;

import java.util.Collection;

public class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean object, String message) {
        if (!object) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        notNull(collection, message);
        isTrue(!collection.isEmpty(), message);
    }

    public static void notEmpty(String string, String message) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
