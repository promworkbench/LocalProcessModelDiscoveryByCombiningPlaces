package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.PlaceBasedLPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.LiftEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LiftCollectorTest {

    private static final double DELTA = 1e-9;
    private static final XFactory FACTORY = XFactoryRegistry.instance().currentDefault();

    private static XTrace trace(String... activities) {
        XTrace trace = FACTORY.createTrace();
        for (String activity : activities) {
            XEvent event = FACTORY.createEvent();
            XConceptExtension.instance().assignName(event, activity);
            trace.add(event);
        }
        return trace;
    }

    private static LiftEvaluationResult discoverLiftOfAbc(int concurrencyLimit, XTrace... traces) {
        XLog log = FACTORY.createLog();
        log.addAll(Arrays.asList(traces));
        Set<Place> places = new HashSet<>(Arrays.asList(MockPlaces.getSequencePlace_ab(),
                MockPlaces.getSequencePlace_bc()));

        LPMDiscoveryResult result = new PlaceBasedLPMDiscovery(l -> PlacesProvider.fromSet(places),
                100, 3, concurrencyLimit).from(log);

        LocalProcessModel abc = result.getAllLPMs().stream()
                .filter(lpm -> lpm.getPlaces().size() == 2)
                .findFirst()
                .orElseThrow(() -> new AssertionError("The a -> b -> c LPM was not discovered"));
        return abc.getAdditionalInfo().getEvaluationResult(
                StandardLPMEvaluationResultId.LiftEvaluationResult.name(), LiftEvaluationResult.class);
    }

    @Test
    public void givenSingleTraceAbc_whenDiscover_thenLiftComputedOverSlidingWindows() {
        // windows with proximity 3: [a] [a,b] [a,b,c] [b,c] [c]
        LiftEvaluationResult lift = discoverLiftOfAbc(1, trace("a", "b", "c"));

        Assert.assertEquals(1, lift.getCount());
        Assert.assertEquals(5, lift.getTotal());
        // (1/5) / (3/5)^3
        Assert.assertEquals(125.0 / 135, lift.getResult(), DELTA);
    }

    @Test
    public void givenConcurrencyCombinations_whenDiscover_thenEachWindowCountedOnce() {
        // with concurrency 2 the same LPM is reported twice for window [a,b,c] (directly and as a branch combination)
        LiftEvaluationResult lift = discoverLiftOfAbc(2, trace("a", "b", "c"));

        Assert.assertEquals(1, lift.getCount());
    }

    @Test
    public void givenMultipleVariants_whenDiscover_thenCountsWeightedByVariantFrequency() {
        // windows (proximity 3), 27 in total:
        // abcxy (x2): [a] [a,b] [a,b,c] [b,c,x] [c,x,y] [x,y] [y]
        // abab:       [a] [a,b] [a,b,a] [b,a,b] [a,b] [b]
        // xayyb:      [x] [x,a] [x,a,y] [a,y,y] [y,y,b] [y,b] [b]
        // windows containing a: 6 + 5 + 3 = 14, b: 6 + 5 + 3 = 14, c: 6
        LiftEvaluationResult lift = discoverLiftOfAbc(2,
                trace("a", "b", "c", "x", "y"),
                trace("a", "b", "c", "x", "y"),
                trace("a", "b", "a", "b"),
                trace("x", "a", "y", "y", "b"));

        Assert.assertEquals(2, lift.getCount());
        Assert.assertEquals(27, lift.getTotal());
        Assert.assertEquals((2.0 / 27) / ((14.0 / 27) * (14.0 / 27) * (6.0 / 27)), lift.getResult(), DELTA);
    }
}
