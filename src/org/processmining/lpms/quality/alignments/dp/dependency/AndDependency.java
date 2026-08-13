package org.processmining.lpms.quality.alignments.dp.dependency;

import java.util.List;

/**
 * All of {@code children} must be satisfied, e.g. a transition requiring a token in
 * multiple distinct input places (concurrency/synchronization).
 */
public class AndDependency implements DependencyExpression {

    private final List<DependencyExpression> children;

    public AndDependency(List<DependencyExpression> children) {
        this.children = children;
    }

    public List<DependencyExpression> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndDependency that = (AndDependency) o;
        return DependencyExpressions.sameMultiset(children, that.children);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + DependencyExpressions.unorderedHash(children);
    }

    @Override
    public String toString() {
        return "AND" + children;
    }
}
