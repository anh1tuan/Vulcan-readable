package me.frep.vulcan.spigot;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionUtils {
    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean classExists(String className) {
        return forName(className) != null;
    }

    @Nullable
    public static Class forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static boolean hasMethod(@NotNull Class type, String name, Class[] parameterTypes) {
        return getMethod(type, name, parameterTypes) != null;
    }

    @Nullable
    public static Method getMethod(@NotNull Class type, @NotNull String name, Class[] parameterTypes) {
        Class current = type;
        while (current != null) {
            try {
                return current.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException ignored) {
                try {
                    Method declared = current.getDeclaredMethod(name, parameterTypes);
                    declared.setAccessible(true);
                    return declared;
                } catch (NoSuchMethodException ignoredDeclared) {
                    current = current.getSuperclass();
                }
            }
        }
        return null;
    }

    public static Field getFieldByTypeName(Class type, String... typeNames) throws NoSuchFieldException {
        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            String simpleName = fields[i].getType().getSimpleName();
            for (int n = 0; n < typeNames.length; ++n) {
                if (typeNames[n].equals(simpleName)) {
                    fields[i].setAccessible(true);
                    return fields[i];
                }
            }
        }
        throw new NoSuchFieldException(
            "Could not find field in class " + type.getName() + " with names " + Arrays.toString(typeNames)
        );
    }

    public static Field getDeclaredField(Class type, Class fieldType) throws NoSuchFieldException {
        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldType.isAssignableFrom(fields[i].getType())) {
                fields[i].setAccessible(true);
                return fields[i];
            }
        }
        throw new NoSuchFieldException(
            "Could not find field in class " + type.getName() + " type " + fieldType.getName()
        );
    }

    public static Object getNonNullFieldValue(Object instance, Class fieldType) throws Exception {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (!fieldType.isAssignableFrom(fields[i].getType())) {
                continue;
            }
            fields[i].setAccessible(true);
            Object value = fields[i].get(instance);
            if (value != null) {
                return value;
            }
        }
        throw new NoSuchFieldException(
            "Could not find non-null field in class " + instance.getClass().getName() + " type " + fieldType.getName()
        );
    }
}
