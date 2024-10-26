package bh.dpl.utils.options;

import java.util.Iterator;

/**
 * @author Kev1nLeft
 */

public class IteratorUtils {
    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable == null) {
            return true;
        }
        Iterator<?> iterator = iterable.iterator();
        return !iterator.hasNext();
    }
}
