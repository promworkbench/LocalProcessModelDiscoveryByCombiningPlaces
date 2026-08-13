package org.processmining.lpms.quality.alignments.dp.dependency;

/**
 * A boolean expression describing which activities a transition directly depends on:
 * {@link ActivityDependency} leaves combined via {@link AndDependency}/{@link OrDependency},
 * or {@link NoDependency} when a transition depends on nothing.
 */
public interface DependencyExpression {
}
