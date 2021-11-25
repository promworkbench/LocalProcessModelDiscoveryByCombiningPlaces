package org.processmining.placebasedlpmdiscovery.placediscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.exports.PlaceSetExportPlugin;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.placebasedlpmdiscovery.utils.ProjectProperties;

import java.io.*;

/**
 * Class that takes care of the place discovery part of the system.
 */
public class PlaceDiscovery {
    /**
     * Discovers places for the given event log using the discovery algorithm sent as second parameter
     *
     * @param log: the event log for which places are discovered
     * @return set of places
     */
    public static PlaceDiscoveryResult discover(XLog log, PlaceDiscoveryParameters parameters) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> algorithm = parameters.getAlgorithm(factory);
        PlaceDiscoveryResult result = algorithm.getPlaces(log);
        PlaceSetExportPlugin.export(Main.getContext(), new PlaceSet(result.getPlaces()),
                new File(ProjectProperties.getProperty(ProjectProperties.PLACE_WRITE_DESTINATION_KEY)));
        if (result.getLog() == null)
            result.setLog(log);
        return result;
    }
}
