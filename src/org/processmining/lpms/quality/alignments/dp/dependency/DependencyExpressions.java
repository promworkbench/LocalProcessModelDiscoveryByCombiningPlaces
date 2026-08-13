package org.processmining.lpms.quality.alignments.dp.dependency;

import java.util.ArrayList;
import java.util.List;

/**
 * Order-independent (multiset) comparison helpers for AND/OR children, since AND/OR are
 * commutative but the underlying Petri net traversal that produces them has no guaranteed order.
 */
class DependencyExpressions {

    private DependencyExpressions() {
    }

    static boolean sameMultiset(List<DependencyExpression> first, List<DependencyExpression> second) {
        if (first.size() != second.size()) {
            return false;
        }
        List<DependencyExpression> remaining = new ArrayList<>(second);
        for (DependencyExpression element : first) {
            if (!remaining.remove(element)) {
                return false;
            }
        }
        return true;
    }

    static int unorderedHash(List<DependencyExpression> children) {
        int hash = 0;
        for (DependencyExpression child : children) {
            hash += child.hashCode();
        }
        return hash;
    }
}
