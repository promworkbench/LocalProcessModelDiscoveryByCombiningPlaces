package org.processmining.lpms.quality.alignments.dp.dependency;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

import java.util.*;

public class SharedDependencyFinderTest {

    private static Transition transition(String label) {
        return new PetrinetImpl("net").addTransition(label);
    }

    @Test
    public void givenLiteralsWithSharedTransitiveDependencies_whenSharedDependencies_thenGroupsByDependency() {
        // given: a, b depend directly on e; c depends on both e and f; d depends directly on f
        // so e is shared by {a, b, c} and f is shared by {c, d}
        Transition a = transition("a");
        Transition b = transition("b");
        Transition c = transition("c");
        Transition d = transition("d");
        Transition e = transition("e");
        Transition f = transition("f");

        Map<Transition, DependencyExpression> dependencies = new HashMap<>();
        dependencies.put(a, new ActivityDependency(e));
        dependencies.put(b, new ActivityDependency(e));
        dependencies.put(c, new AndDependency(Arrays.asList(new ActivityDependency(e), new ActivityDependency(f))));
        dependencies.put(d, new ActivityDependency(f));
        dependencies.put(e, SingleExecutionDependency.INSTANCE);
        dependencies.put(f, SingleExecutionDependency.INSTANCE);

        // when
        Map<Transition, Set<Transition>> shared = SharedDependencyFinder.sharedDependencies(
                Arrays.asList(a, b, c, d), dependencies);

        // then
        Map<Transition, Set<Transition>> expected = new HashMap<>();
        expected.put(e, new LinkedHashSet<>(Arrays.asList(a, b, c)));
        expected.put(f, new LinkedHashSet<>(Arrays.asList(c, d)));

        Assertions.assertThat(shared).isEqualTo(expected);
    }

    @Test
    public void givenSelfLoopDependency_whenSharedDependencies_thenTerminatesAndExcludesLiteralItself() {
        // given: p depends on itself, and both q and r depend on p - p should be reported as
        // shared by {q, r}, but never as a dependency of itself
        Transition p = transition("p");
        Transition q = transition("q");
        Transition r = transition("r");

        Map<Transition, DependencyExpression> dependencies = new HashMap<>();
        dependencies.put(p, new ActivityDependency(p));
        dependencies.put(q, new ActivityDependency(p));
        dependencies.put(r, new ActivityDependency(p));

        // when
        Map<Transition, Set<Transition>> shared = SharedDependencyFinder.sharedDependencies(
                Arrays.asList(p, q, r), dependencies);

        // then
        Map<Transition, Set<Transition>> expected = new HashMap<>();
        expected.put(p, new LinkedHashSet<>(Arrays.asList(q, r)));

        Assertions.assertThat(shared).isEqualTo(expected);
    }

    @Test
    public void givenChainedSharedDependency_whenSharedDependencies_thenOnlyClosestIsReturned() {
        // given: a, d both depend on b, and b depends on c - c is only shared transitively
        // through b, so only b (the closer join point) should be reported
        Transition a = transition("a");
        Transition b = transition("b");
        Transition c = transition("c");
        Transition d = transition("d");

        Map<Transition, DependencyExpression> dependencies = new HashMap<>();
        dependencies.put(a, new ActivityDependency(b));
        dependencies.put(d, new ActivityDependency(b));
        dependencies.put(b, new ActivityDependency(c));
        dependencies.put(c, SingleExecutionDependency.INSTANCE);

        // when
        Map<Transition, Set<Transition>> shared = SharedDependencyFinder.sharedDependencies(
                Arrays.asList(a, d), dependencies);

        // then
        Map<Transition, Set<Transition>> expected = new HashMap<>();
        expected.put(b, new LinkedHashSet<>(Arrays.asList(a, d)));

        Assertions.assertThat(shared).isEqualTo(expected);
    }

    @Test
    public void givenTwoCycleBetweenLiterals_whenSharedDependencies_thenTerminatesWithNoFalseSharing() {
        // given: p and q depend on each other directly, and are themselves the only literals -
        // each only transitively depends on the other, never on itself, so no dependency is
        // actually shared by two or more literals
        Transition p = transition("p");
        Transition q = transition("q");

        Map<Transition, DependencyExpression> dependencies = new HashMap<>();
        dependencies.put(p, new ActivityDependency(q));
        dependencies.put(q, new ActivityDependency(p));

        // when
        Map<Transition, Set<Transition>> shared = SharedDependencyFinder.sharedDependencies(
                Arrays.asList(p, q), dependencies);

        // then
        Assertions.assertThat(shared).isEmpty();
    }
}
