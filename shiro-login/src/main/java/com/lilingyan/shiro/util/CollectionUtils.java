package com.lilingyan.shiro.util;

import com.lilingyan.shiro.subject.PrincipalCollection;

import java.util.*;

/**
 * Static helper class for use dealing with Collections.
 *
 * @since 0.9
 */
public class CollectionUtils {

    public static <E> Set<E> asSet(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }

        if (elements.length == 1) {
            return Collections.singleton(elements[0]);
        }

        LinkedHashSet<E> set = new LinkedHashSet<E>(elements.length * 4 / 3 + 1);
        Collections.addAll(set, elements);
        return set;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();
    }

    public static int size(Collection c) {
        return c != null ? c.size() : 0;
    }

    public static int size(Map m) {
        return m != null ? m.size() : 0;
    }


    public static <E> List<E> asList(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptyList();
        }

        // Integer overflow does not occur when a large array is passed in because the list array already exists
        return Arrays.asList(elements);
    }

    static int computeListCapacity(int arraySize) {
        return (int) Math.min(5L + arraySize + (arraySize / 10), Integer.MAX_VALUE);
    }

    public static boolean isEmpty(PrincipalCollection principals) {
        return principals == null || principals.isEmpty();
    }

}
