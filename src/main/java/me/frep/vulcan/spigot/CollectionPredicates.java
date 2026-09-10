package me.frep.vulcan.spigot;

import java.util.Collection;
import java.util.function.Predicate;

public final class CollectionPredicates {
    public static boolean exists(Collection collection, Predicate predicate) {
        if (collection == null || predicate == null) {
            return false;
        }
        for (Object element : collection) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matches(Collection collection, Predicate predicate) {
        if (collection == null || predicate == null) {
            return false;
        }
        for (Object element : collection) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    private CollectionPredicates() {
        throw new UnsupportedOperationException("CollectionPredicates is a utility class and cannot be instantiated");
    }
}
