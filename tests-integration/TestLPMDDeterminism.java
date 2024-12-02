import org.junit.Assert;
import org.junit.Test;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
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
        Petrinet petrinet = PlaceUtils.extractPetriNet("./data/petrinets/artificialBig.pnml");

        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog,
                        new PetriNetPlaceDiscovery(petrinet).getPlaces().getPlaces()));
        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(eventLog);

        // act: run discovery given the input
        LPMDiscoveryResult expected = LPMDiscoveryResult.fromFile("./data/lpms/artificialBig.json");
        LPMDiscoveryResult actual = LPMDiscoveryPlugin.getLpmDiscoveryResult(input, parameters);

        // assert: the same result should be returned as the saved lpms
        Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
        Assert.assertEquals(expected.getAllLPMs(), actual.getAllLPMs());
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

        // act: run discovery given the input
        LPMDiscoveryResult expected = LPMDiscoveryResult.fromFile("./data/lpms/bpi2012_res10939.json");
        LPMDiscoveryResult actual = LPMDiscoveryPlugin.getLpmDiscoveryResult(input, parameters);

        // assert: the same result should be returned as the saved lpms
        Assert.assertEquals(expected.getAllLPMs().size(), actual.getAllLPMs().size());
        Assert.assertEquals(expected.getAllLPMs(), actual.getAllLPMs());
    }
}
