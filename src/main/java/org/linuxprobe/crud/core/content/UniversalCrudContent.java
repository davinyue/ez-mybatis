package org.linuxprobe.crud.core.content;

import java.util.HashMap;
import java.util.Map;

public class UniversalCrudContent {
	private static Map<Class<?>, EntityInfo> entityInfos = new HashMap<>();

	public static void addEntityInfo(Class<?> entityType) {
		entityInfos.put(entityType, new EntityInfo(entityType));
	}

	public static EntityInfo getEntityInfo(Class<?> entityType) {
		return entityInfos.get(entityType);
	}
}
