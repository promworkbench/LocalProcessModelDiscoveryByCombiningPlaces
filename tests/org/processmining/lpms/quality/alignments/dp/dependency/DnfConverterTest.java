package org.processmining.lpms.quality.alignments.dp.dependency;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

import java.util.Arrays;
import java.util.Collections;

public class DnfConverterTest {

    private static Transition transition(String label) {
        return new PetrinetImpl("net").addTransition(label);
    }

    @Test
    public void givenLeaf_whenToDnf_thenUnchanged() {
        ActivityDependency a = new ActivityDependency(transition("a"));

        Assertions.assertThat(DnfConverter.toDnf(a)).isEqualTo(a);
        Assertions.assertThat(DnfConverter.toDnf(SingleExecutionDependency.INSTANCE))
                .isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(DnfConverter.toDnf(NoDependency.INSTANCE)).isEqualTo(NoDependency.INSTANCE);
    }

    @Test
    public void givenOrOfLeaves_whenToDnf_thenUnchanged() {
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        OrDependency expression = new OrDependency(Arrays.asList(a, b));

        Assertions.assertThat(DnfConverter.toDnf(expression)).isEqualTo(expression);
    }

    @Test
    public void givenAndOfOrs_whenToDnf_thenDistributesIntoOrOfAnds() {
        // given: AND(OR(a, b), OR(c, d))
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        ActivityDependency c = new ActivityDependency(transition("c"));
        ActivityDependency d = new ActivityDependency(transition("d"));

        AndDependency cnf = new AndDependency(Arrays.asList(
                new OrDependency(Arrays.asList(a, b)),
                new OrDependency(Arrays.asList(c, d))));

        // when
        DependencyExpression dnf = DnfConverter.toDnf(cnf);

        // then: OR(AND(a, c), AND(a, d), AND(b, c), AND(b, d))
        OrDependency expected = new OrDependency(Arrays.asList(
                new AndDependency(Arrays.asList(a, c)),
                new AndDependency(Arrays.asList(a, d)),
                new AndDependency(Arrays.asList(b, c)),
                new AndDependency(Arrays.asList(b, d))));

        Assertions.assertThat(dnf).isEqualTo(expected);
    }

    @Test
    public void givenAndWithThreeWayOr_whenToDnf_thenEachDisjunctIsSingleLiteral() {
        // given: AND(OR(a, b, c)) collapses to a single-child AND, still exercises the distribution path
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        ActivityDependency c = new ActivityDependency(transition("c"));

        AndDependency cnf = new AndDependency(Collections.singletonList(
                new OrDependency(Arrays.asList(a, b, c))));

        // when
        DependencyExpression dnf = DnfConverter.toDnf(cnf);

        // then
        Assertions.assertThat(dnf).isEqualTo(new OrDependency(Arrays.asList(a, b, c)));
    }

    @Test
    public void givenDeeplyNestedOrOfAndOfOr_whenToDnf_thenFullyFlattensNestedLevels() {
        // given: OR( AND( a, OR(b, c) ), d )  -- nesting beyond the direct-dependency 2-level shape
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        ActivityDependency c = new ActivityDependency(transition("c"));
        ActivityDependency d = new ActivityDependency(transition("d"));

        OrDependency nested = new OrDependency(Arrays.asList(
                new AndDependency(Arrays.asList(a, new OrDependency(Arrays.asList(b, c)))),
                d));

        // when
        DependencyExpression dnf = DnfConverter.toDnf(nested);

        // then: OR(AND(a, b), AND(a, c), d)
        OrDependency expected = new OrDependency(Arrays.asList(
                new AndDependency(Arrays.asList(a, b)),
                new AndDependency(Arrays.asList(a, c)),
                d));

        Assertions.assertThat(dnf).isEqualTo(expected);
    }

    @Test
    public void givenRepeatedLiteralAcrossBranches_whenToDnf_thenConjunctionDedupes() {
        // given: AND(OR(a, b), OR(a, c)) -- combination (a, a) should collapse to a single literal
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        ActivityDependency c = new ActivityDependency(transition("c"));

        AndDependency cnf = new AndDependency(Arrays.asList(
                new OrDependency(Arrays.asList(a, b)),
                new OrDependency(Arrays.asList(a, c))));

        // when
        DependencyExpression dnf = DnfConverter.toDnf(cnf);

        // then: OR(a, AND(a, c), AND(b, a), AND(b, c))  -- the (a, a) combination dedupes to just a
        OrDependency expected = new OrDependency(Arrays.asList(
                a,
                new AndDependency(Arrays.asList(a, c)),
                new AndDependency(Arrays.asList(b, a)),
                new AndDependency(Arrays.asList(b, c))));

        Assertions.assertThat(dnf).isEqualTo(expected);
    }

    @Test
    public void givenSharedStartChoiceDependencies_whenToDnf_thenDistributesWithoutAbsorption() {
        // mirrors DirectDependencyComputerTest
        // #givenSharedStartChoiceWithAndJoins_whenCompute_thenDAndEDependOnRedundantAndOfOrAndSingleBranch
        // given: d's dependency is AND(OR(a, b), a) and e's is AND(OR(a, b), b) - each AND repeats a
        // literal already present in its OR child, so a naive reader might expect absorption
        // (AND(OR(a,b), a) = a); toDnf does NOT perform absorption, it purely distributes.
        ActivityDependency a = new ActivityDependency(transition("a"));
        ActivityDependency b = new ActivityDependency(transition("b"));
        OrDependency aOrB = new OrDependency(Arrays.asList(a, b));

        AndDependency dDependency = new AndDependency(Arrays.asList(aOrB, a));
        AndDependency eDependency = new AndDependency(Arrays.asList(aOrB, b));

        // when
        DependencyExpression dDnf = DnfConverter.toDnf(dDependency);
        DependencyExpression eDnf = DnfConverter.toDnf(eDependency);

        // then: OR(a, AND(b, a)) and OR(AND(a, b), b) respectively
        OrDependency dExpected = new OrDependency(Arrays.asList(a, new AndDependency(Arrays.asList(b, a))));
        OrDependency eExpected = new OrDependency(Arrays.asList(new AndDependency(Arrays.asList(a, b)), b));

        Assertions.assertThat(dDnf).isEqualTo(dExpected);
        Assertions.assertThat(eDnf).isEqualTo(eExpected);
    }
}