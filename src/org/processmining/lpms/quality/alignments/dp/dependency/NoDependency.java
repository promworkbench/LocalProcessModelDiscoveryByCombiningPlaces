package org.processmining.lpms.quality.alignments.dp.dependency;

/**
 * Marks a transition that depends on nothing, e.g. an initial activity fed only by
 * a source place with no producers.
 */
public final class NoDependency implements DependencyExpression {

    public static final NoDependency INSTANCE = new NoDependency();

    private NoDependency() {
    }

    @Override
    public String toString() {
        return "NONE";
    }
}
