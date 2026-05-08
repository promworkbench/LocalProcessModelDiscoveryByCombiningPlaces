import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.LPMDiscoveryPlugin;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestLPMDDeterminism {

    @Test
    public void testDeterminismForBpi2012Res10939() throws Exception {
        // TODO (Newest): The test is failing because there was an error in PassageUsagePlaceTransformer. However,
        //  now the question is whether just to update the saved file or find a way in which some tracking of the
        //  obtained results can be made
        // arrange: prepare input
        EventLog eventLog = new XLogWrapper(LogUtils.readLogFromFile("./data/logs/bpi2012_res10939.xes"));
        Set<Place> places = PlaceUtils.extractPlaceNets("./data/placenets/bpi2012_res10939.json");

        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog,
                        new PlaceSet(places).getPlaces().getPlaces()));
        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(eventLog);

        LPMDiscoveryResult expected = LPMDiscoveryResult.fromFile("./data/test/lpms/bpi2012_res10939.json");

        for (int i = 0; i < 10; i++) {
            // act: run discovery given the input
            LPMDiscoveryResult actual = LPMDiscoveryPlugin.getLpmDiscoveryResult(input, parameters);

            // assert: the same result should be returned as the saved lpms
            Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
            Assertions.assertThat(expected.getAllLPMs()).hasSameElementsAs(actual.getAllLPMs());
        }
    }

    @Test
    public void testDeterminismWithOnTheFlyLog() {
        // arrange: 10-variant log and 10 places, all created in memory — no files needed
        XLogWrapper logWrapper = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b", "c", "d", "e"),
                Arrays.asList("a", "b", "d", "c", "e"),
                Arrays.asList("a", "c", "b", "d", "e"),
                Arrays.asList("b", "a", "c", "d", "e"),
                Arrays.asList("a", "b", "c", "e", "d"),
                Arrays.asList("a", "c", "d", "b", "e"),
                Arrays.asList("b", "c", "a", "d", "e"),
                Arrays.asList("a", "d", "b", "c", "e"),
                Arrays.asList("b", "a", "d", "c", "e"),
                Arrays.asList("a", "b", "d", "e", "c")
        ));

        PlacesProvider placesProvider = PlacesProvider.fromSet(new HashSet<>(Arrays.asList(
                Place.from("a | b"),
                Place.from("a | c"),
                Place.from("a | d"),
                Place.from("a | e"),
                Place.from("b | c"),
                Place.from("b | d"),
                Place.from("b | e"),
                Place.from("c | d"),
                Place.from("c | e"),
                Place.from("d | e")
        )));

        // act: first run establishes the expected result
        LPMDiscoveryResult expected = LPMDiscovery.placeBased(placesProvider).from(logWrapper.getOriginalLog());
        System.out.println("Expected LPMs: "  + expected.getAllLPMs().size());

        for (int i = 0; i < 100; i++) {
            // act: run discovery again
            LPMDiscoveryResult actual = LPMDiscovery.placeBased(placesProvider).from(logWrapper.getOriginalLog());

            // assert: every run must produce the same result
            Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
            Assertions.assertThat(actual.getAllLPMs()).hasSameElementsAs(expected.getAllLPMs());
        }
    }

    @Test
    public void testDeterminismWithLargerOnTheFlyLog() {
        // arrange: 50-variant log over {a,b,c,d,e,f} and 20 places (mix of single and multi-arc)
        XLogWrapper logWrapper = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                // full 6-activity permutations
                Arrays.asList("a", "b", "c", "d", "e", "f"),
                Arrays.asList("a", "b", "c", "d", "f", "e"),
                Arrays.asList("a", "b", "c", "e", "d", "f"),
                Arrays.asList("a", "b", "c", "e", "f", "d"),
                Arrays.asList("a", "b", "c", "f", "d", "e"),
                Arrays.asList("a", "b", "d", "c", "e", "f"),
                Arrays.asList("a", "b", "d", "c", "f", "e"),
                Arrays.asList("a", "b", "d", "e", "c", "f"),
                Arrays.asList("a", "b", "e", "c", "d", "f"),
                Arrays.asList("a", "b", "f", "c", "d", "e"),
                Arrays.asList("a", "c", "b", "d", "e", "f"),
                Arrays.asList("a", "c", "b", "e", "d", "f"),
                Arrays.asList("a", "c", "d", "b", "e", "f"),
                Arrays.asList("a", "c", "e", "b", "d", "f"),
                Arrays.asList("a", "d", "b", "c", "e", "f"),
                Arrays.asList("b", "a", "c", "d", "e", "f"),
                Arrays.asList("b", "a", "c", "e", "d", "f"),
                Arrays.asList("b", "a", "d", "c", "e", "f"),
                Arrays.asList("b", "c", "a", "d", "e", "f"),
                Arrays.asList("b", "c", "d", "a", "e", "f"),
                // 5-activity sub-sequences
                Arrays.asList("a", "b", "c", "d", "e"),
                Arrays.asList("a", "b", "c", "d", "f"),
                Arrays.asList("a", "b", "c", "e", "f"),
                Arrays.asList("a", "b", "d", "e", "f"),
                Arrays.asList("a", "c", "d", "e", "f"),
                Arrays.asList("b", "c", "d", "e", "f"),
                Arrays.asList("a", "b", "d", "c", "f"),
                Arrays.asList("a", "c", "b", "e", "f"),
                Arrays.asList("b", "a", "d", "e", "f"),
                Arrays.asList("a", "b", "e", "d", "f"),
                // traces with repeated activities
                Arrays.asList("a", "b", "a", "c", "d", "e"),
                Arrays.asList("a", "b", "c", "b", "d", "e"),
                Arrays.asList("a", "b", "c", "d", "c", "e"),
                Arrays.asList("a", "b", "c", "d", "e", "d"),
                Arrays.asList("a", "b", "c", "d", "e", "a"),
                Arrays.asList("b", "a", "b", "c", "d", "e"),
                Arrays.asList("a", "c", "a", "b", "d", "e"),
                Arrays.asList("a", "b", "d", "b", "c", "e"),
                Arrays.asList("a", "b", "c", "e", "c", "f"),
                Arrays.asList("a", "b", "c", "d", "f", "d"),
                // longer traces
                Arrays.asList("a", "b", "c", "d", "e", "f", "a", "b"),
                Arrays.asList("a", "b", "c", "d", "e", "b", "c", "f"),
                Arrays.asList("a", "b", "a", "c", "d", "b", "e", "f"),
                Arrays.asList("a", "b", "c", "d", "a", "b", "c", "e"),
                Arrays.asList("b", "c", "d", "e", "f", "a", "b", "c"),
                // short traces
                Arrays.asList("a", "b", "c"),
                Arrays.asList("b", "c", "d"),
                Arrays.asList("c", "d", "e"),
                Arrays.asList("d", "e", "f"),
                Arrays.asList("a", "c", "e")
        ));

        PlacesProvider placesProvider = PlacesProvider.fromSet(new HashSet<>(Arrays.asList(
                // simple: 1 input, 1 output
                Place.from("a | b"),
                Place.from("b | c"),
                Place.from("c | d"),
                Place.from("d | e"),
                Place.from("e | f"),
                Place.from("a | f"),
                // 2 inputs, 1 output
                Place.from("a, b | c"),
                Place.from("b, c | d"),
                Place.from("c, d | e"),
                Place.from("d, e | f"),
                // 1 input, 2 outputs
                Place.from("a | b, c"),
                Place.from("b | c, d"),
                Place.from("c | d, e"),
                Place.from("d | e, f"),
                // 2 inputs, 2 outputs
                Place.from("a, b | c, d"),
                Place.from("b, c | d, e"),
                Place.from("c, d | e, f"),
                Place.from("a, c | b, d"),
                Place.from("b, d | c, e"),
                Place.from("a, e | b, f")
        )));

        // act: first run establishes the expected result
        LPMDiscoveryResult expected = LPMDiscovery.placeBased(placesProvider).from(logWrapper.getOriginalLog());
        System.out.println("Expected LPMs: " + expected.getAllLPMs().size());

        for (int i = 0; i < 20; i++) {
            // act: run discovery again
            LPMDiscoveryResult actual = LPMDiscovery.placeBased(placesProvider).from(logWrapper.getOriginalLog());

            // assert: every run must produce the same result
            Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
            Assertions.assertThat(actual.getAllLPMs()).hasSameElementsAs(expected.getAllLPMs());
        }
    }
}
