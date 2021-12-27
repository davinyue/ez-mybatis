package org.rdlinux.ezmybatis.core.utils;

import java.util.Collection;

public class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "object cannot be null");
    }

    public static void isTrue(boolean object, String message) {
        if (!object) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean object) {
        isTrue(object, "object must be true");
    }

    public static void notEmpty(Collection<?> collection) {
        notNull(collection, "collection cannot be null");
        isTrue(!collection.isEmpty(), "collection cannot be empty");
    }


    public static void notEmpty(Collection<?> collection, String message) {
        notNull(collection, message);
        isTrue(!collection.isEmpty(), message);
    }
}
