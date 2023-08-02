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
public interface PlaceDiscovery {

    PlaceDiscoveryResult getPlaces();
}
