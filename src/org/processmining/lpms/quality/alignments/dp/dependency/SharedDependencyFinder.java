package org.processmining.lpms.quality.alignments.dp.dependency;

import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Given a set of literal transitions (e.g. the members of an {@link AndDependency}) and the
 * per-transition {@link DependencyExpression} map produced by
 * {@code DirectDependencyComputer.compute}, finds the transitions that multiple literals
 * transitively depend on, together with which literals share each one.
 */
public final class SharedDependencyFinder {

    private SharedDependencyFinder() {
    }

    // flattens AND/OR into the set of transitions directly referenced, ignoring leaves with no transition
    private static Set<Transition> referencedTransitions(DependencyExpression expression) {
        if (expression instanceof ActivityDependency) {
            return Collections.singleton(((ActivityDependency) expression).getTransition());
        }
        if (expression instanceof AndDependency) {
            return ((AndDependency) expression).getChildren().stream()
                    .flatMap(child -> referencedTransitions(child).stream())
                    .collect(Collectors.toSet());
        }
        if (expression instanceof OrDependency) {
            return ((OrDependency) expression).getChildren().stream()
                    .flatMap(child -> referencedTransitions(child).stream())
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * All transitions transitively reachable by walking {@code dependencies} backward from
     * {@code start}, excluding {@code start} itself. Marks each transition as visited before it
     * is queued, so self-loops and longer cycles back to {@code start} or to any transition
     * already on the path are only ever expanded once and cannot cause an infinite loop.
     */
    private static Set<Transition> transitiveDependencies(Transition start,
                                                          Map<Transition, DependencyExpression> dependencies) {
        Set<Transition> visited = new LinkedHashSet<>();
        visited.add(start);
        Deque<Transition> queue = new ArrayDeque<>(Collections.singletonList(start));
        while (!queue.isEmpty()) {
            Transition current = queue.poll();
            DependencyExpression expression = dependencies.get(current);
            if (expression == null) {
                continue;
            }
            for (Transition next : referencedTransitions(expression)) {
                if (visited.add(next)) {
                    queue.add(next);
                }
            }
        }
        // start was only seeded to guard against it re-appearing via a cycle; it is not a
        // dependency of itself.
        visited.remove(start);
        return visited;
    }

    /**
     * For each transition transitively depended on by two or more of {@code literals}, the
     * subset of {@code literals} that depend on it - e.g. for literals a, b, c, d where a, b, c
     * all transitively depend on e and c, d both transitively depend on f, this returns
     * {@code {e: {a, b, c}, f: {c, d}}}. If a shared transition is only reachable through
     * another, nearer shared transition that covers at least the same literals, only the nearer
     * one is kept - e.g. for literals a, d that both depend on b, and b depends on c, this
     * returns {@code {b: {a, d}}}, not {@code c}.
     */
    public static Map<Transition, Set<Transition>> sharedDependencies(List<Transition> literals,
                                                                      Map<Transition, DependencyExpression> dependencies) {
        Map<Transition, Set<Transition>> sharedBy = new LinkedHashMap<>();
        for (Transition literal : literals) {
            for (Transition dependency : transitiveDependencies(literal, dependencies)) {
                sharedBy.computeIfAbsent(dependency, d -> new LinkedHashSet<>()).add(literal);
            }
        }
        sharedBy.values().removeIf(sharingLiterals -> sharingLiterals.size() < 2);
        removeDominated(sharedBy, dependencies);
        return sharedBy;
    }

    /**
     * Drops entries of {@code sharedBy} that are only reachable through another, nearer entry
     * covering at least the same literals - only the closest shared join point per set of
     * literals is kept. A transition is not considered nearer than one it is itself
     * transitively reachable from, so a cycle between two equally-shared transitions leaves both
     * in place.
     */
    private static void removeDominated(Map<Transition, Set<Transition>> sharedBy,
                                        Map<Transition, DependencyExpression> dependencies) {
        Set<Transition> dominated = new LinkedHashSet<>();
        for (Map.Entry<Transition, Set<Transition>> closer : sharedBy.entrySet()) {
            for (Transition further : transitiveDependencies(closer.getKey(), dependencies)) {
                Set<Transition> furtherLiterals = sharedBy.get(further);
                if (furtherLiterals == null || !closer.getValue().containsAll(furtherLiterals)) {
                    continue;
                }
                if (transitiveDependencies(further, dependencies).contains(closer.getKey())) {
                    continue;
                }
                dominated.add(further);
            }
        }
        sharedBy.keySet().removeAll(dominated);
    }
}
