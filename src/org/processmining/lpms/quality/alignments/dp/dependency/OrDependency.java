package org.processmining.lpms.quality.alignments.dp.dependency;

import java.util.List;

/**
 * Any one of {@code children} suffices, e.g. a place fed by multiple alternative
 * producer transitions (choice/merge).
 */
public class OrDependency implements DependencyExpression {

    private final List<DependencyExpression> children;

    public OrDependency(List<DependencyExpression> children) {
        this.children = children;
    }

    public List<DependencyExpression> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrDependency that = (OrDependency) o;
        return DependencyExpressions.sameMultiset(children, that.children);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + DependencyExpressions.unorderedHash(children);
    }

    @Override
    public String toString() {
        return "OR" + children;
    }
}
