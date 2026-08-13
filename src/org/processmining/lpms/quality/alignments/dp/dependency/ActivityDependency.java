package org.processmining.lpms.quality.alignments.dp.dependency;

import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

import java.util.Objects;

public class ActivityDependency implements DependencyExpression {

    private final Transition transition;

    public ActivityDependency(Transition transition) {
        this.transition = transition;
    }

    public Transition getTransition() {
        return transition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityDependency that = (ActivityDependency) o;
        return Objects.equals(transition, that.transition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transition);
    }

    @Override
    public String toString() {
        return transition.getLabel();
    }
}
