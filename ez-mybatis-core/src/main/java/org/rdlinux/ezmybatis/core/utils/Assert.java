package org.rdlinux.ezmybatis.core.utils;

import java.util.Collection;

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

    public static void notEmpty(Collection<?> collection) {
        notNull(collection, "collection can not be null");
        isTrue(!collection.isEmpty(), "collection can not be empty");
    }


    public static void notEmpty(Collection<?> collection, String message) {
        notNull(collection, message);
        isTrue(!collection.isEmpty(), message);
    }
}
