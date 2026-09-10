package me.frep.vulcan.spigot;

import java.util.concurrent.ConcurrentHashMap;

public final class ClassNameRegistry {
    private static final ConcurrentHashMap<String, String> CLASS_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> METHOD_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FIELD_MAP = new ConcurrentHashMap<>();

    private ClassNameRegistry() {}

    public static String remapClassName(String string) {
        return CLASS_MAP.getOrDefault(string, string);
    }

    public static String remapMethodName(String string, Class<?> clazz, Class<?>[] classArray) {
        return METHOD_MAP.getOrDefault(string, string);
    }

    public static String remapFieldName(Class<?> clazz, String string) {
        return FIELD_MAP.getOrDefault(string, string);
    }

    public static void registerBulkEntries(ConcurrentHashMap<String, String> map, String string) {
        if (map != null && string != null) {
            map.put(string, string);
        }
    }
}
