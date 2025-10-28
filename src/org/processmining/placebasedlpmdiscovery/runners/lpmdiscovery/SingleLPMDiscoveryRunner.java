package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.Set;

public class SingleLPMDiscoveryRunner {

    public static void main(String[] args) throws Exception {
        String logPath = "data/logs/artificialBig.xes";
        String placesPath = "data/petrinets/artificialBig.pnml";
        String resultPath = "data/lpms/artificialBig.json";
        EventLog eventLog = new XLogWrapper(LogUtils.readLogFromFile(logPath));

        LPMDiscoveryResult result = LPMDiscovery.placeBased(PlacesProvider.fromFile(placesPath))
                .from(eventLog.getOriginalLog());
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
