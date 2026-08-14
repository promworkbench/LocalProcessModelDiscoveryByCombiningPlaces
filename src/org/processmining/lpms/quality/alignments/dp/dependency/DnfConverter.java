package org.processmining.lpms.quality.alignments.dp.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Converts a {@link DependencyExpression} tree of arbitrary AND/OR nesting into an
 * equivalent disjunctive normal form: an {@link OrDependency} whose children are each
 * either a single leaf or an {@link AndDependency} of leaves. Leaves ({@link ActivityDependency},
 * {@link SingleExecutionDependency}, {@link NoDependency}, or any other non-AND/OR type) pass
 * through unchanged.
 * <p>
 * Conversion is the standard distribute-AND-over-OR rewrite, so the number of disjuncts can grow
 * as the product of the branching factors of the ORs being distributed across - exponential in
 * the worst case.
 */
public final class DnfConverter {

    private DnfConverter() {
    }

    public static DependencyExpression toDnf(DependencyExpression expression) {
        if (expression instanceof OrDependency) {
            List<DependencyExpression> disjuncts = new ArrayList<>();
            for (DependencyExpression child : ((OrDependency) expression).getChildren()) {
                disjuncts.addAll(disjunctsOf(toDnf(child)));
            }
            return orOf(disjuncts);
        }

        if (expression instanceof AndDependency) {
            List<List<DependencyExpression>> perChildDisjuncts = new ArrayList<>();
            for (DependencyExpression child : ((AndDependency) expression).getChildren()) {
                perChildDisjuncts.add(disjunctsOf(toDnf(child)));
            }

            List<DependencyExpression> disjuncts = new ArrayList<>();
            for (List<DependencyExpression> combination : crossProduct(perChildDisjuncts)) {
                LinkedHashSet<DependencyExpression> literals = new LinkedHashSet<>();
                for (DependencyExpression term : combination) {
                    literals.addAll(literalsOf(term));
                }
                disjuncts.add(andOf(new ArrayList<>(literals)));
            }
            return orOf(disjuncts);
        }

        return expression;
    }

    private static List<DependencyExpression> disjunctsOf(DependencyExpression dnf) {
        if (dnf instanceof OrDependency) {
            return ((OrDependency) dnf).getChildren();
        }
        return Collections.singletonList(dnf);
    }

    private static List<DependencyExpression> literalsOf(DependencyExpression term) {
        if (term instanceof AndDependency) {
            return ((AndDependency) term).getChildren();
        }
        return Collections.singletonList(term);
    }

    private static DependencyExpression andOf(List<DependencyExpression> literals) {
        return literals.size() == 1 ? literals.get(0) : new AndDependency(literals);
    }

    private static DependencyExpression orOf(List<DependencyExpression> disjuncts) {
        return disjuncts.size() == 1 ? disjuncts.get(0) : new OrDependency(disjuncts);
    }

    private static List<List<DependencyExpression>> crossProduct(List<List<DependencyExpression>> lists) {
        List<List<DependencyExpression>> combinations = new ArrayList<>();
        combinations.add(new ArrayList<>());
        for (List<DependencyExpression> list : lists) {
            List<List<DependencyExpression>> next = new ArrayList<>();
            for (List<DependencyExpression> prefix : combinations) {
                for (DependencyExpression element : list) {
                    List<DependencyExpression> combined = new ArrayList<>(prefix);
                    combined.add(element);
                    next.add(combined);
                }
            }
            combinations = next;
        }
        return combinations;
    }
}