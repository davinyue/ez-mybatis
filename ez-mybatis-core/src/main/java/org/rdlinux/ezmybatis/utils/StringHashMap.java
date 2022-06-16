package org.rdlinux.ezmybatis.utils;

import java.util.HashMap;
import java.util.Map;

public class StringHashMap extends HashMap<String, Object> {
    private static final long serialVersionUID = -6733238642440700407L;

    public StringHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public StringHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public StringHashMap() {
    }

    public StringHashMap(Map<String, Object> m) {
        super(m);
    }
}
