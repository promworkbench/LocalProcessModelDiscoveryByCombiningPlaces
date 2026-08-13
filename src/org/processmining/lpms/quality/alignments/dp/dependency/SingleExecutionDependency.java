package org.processmining.lpms.quality.alignments.dp.dependency;

/**
 * A place already holds a token in the initial marking, so the transitions consuming from
 * it can fire once "for free" without any producer having fired first. Unlike
 * {@link NoDependency}, this does not mean the branch can be satisfied an unbounded number
 * of times - only once per initial token.
 */
public final class SingleExecutionDependency implements DependencyExpression {

    public static final SingleExecutionDependency INSTANCE = new SingleExecutionDependency();

    private SingleExecutionDependency() {
    }

    @Override
    public String toString() {
        return "SINGLE";
    }
}
