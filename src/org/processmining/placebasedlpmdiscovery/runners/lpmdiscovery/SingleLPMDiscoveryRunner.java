package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.parameters.adapters.ParameterAdaptersUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.Set;

public class SingleLPMDiscoveryRunner {

    public static void main(String[] args) throws Exception {
        String logPath = "data/logs/bpi2012_res10939.xes";
        String placesPath = "data/placenets/bpi2012_res10939.json";
        String resultPath = "data/lpms/bpi2012_res10939.json";
        EventLog eventLog = new XLogWrapper(LogUtils.readLogFromFile(logPath));

        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(eventLog);
        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog, readPlaces(placesPath)));

        PlaceBasedLPMDiscoveryParameters algParam = ParameterAdaptersUtils.transform(parameters, input.getLog());
        LPMDiscoveryAlgBuilder builder = Main.createDefaultBuilder(input.getLog().getOriginalLog(), algParam);

        LPMDiscoveryResult result = builder.build().run(input, algParam);
        result.toFile(resultPath);
    }

    private static Set<Place> readPlaces(String placesPath) throws Exception {
        if (placesPath.endsWith("pnml")) {
            return new PetriNetPlaceDiscovery(PlaceUtils.extractPetriNet(placesPath)).getPlaces().getPlaces();
        } else if (placesPath.endsWith("json")) {
            return PlaceUtils.extractPlaceNets(placesPath);
        }
        return null;
    }
}
