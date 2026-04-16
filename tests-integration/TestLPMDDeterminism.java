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
import org.processmining.placebasedlpmdiscovery.prom.FromFilePlacesProvider;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.LPMDiscoveryPlugin;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.Set;

public class TestLPMDDeterminism {

    @Test
    public void testDeterminismForArtificialBig() throws Exception {
        // arrange: prepare input
        EventLog eventLog = new XLogWrapper(LogUtils.readLogFromFile("./data/logs/artificialBig.xes"));
        PlacesProvider placesProvider = new FromFilePlacesProvider("./data/petrinets/artificialBig.pnml");

        LPMDiscoveryResult expected = LPMDiscoveryResult.fromFile("./data/test/lpms/artificialBig.json");
        // act: run discovery given the input
        LPMDiscoveryResult actual = LPMDiscovery.placeBased(placesProvider).from(eventLog.getOriginalLog());

        // assert: the same result should be returned as the saved lpms
        Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
        Assertions.assertThat(expected.getAllLPMs()).hasSameElementsAs(actual.getAllLPMs());
    }

    @Test
    public void testDeterminismForBpi2012Res10939() throws Exception {
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
}
