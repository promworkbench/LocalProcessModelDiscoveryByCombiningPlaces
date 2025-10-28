package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;

import java.util.Set;

public class FromLogPlacesProvider implements PlacesProvider {

    private final XLog log;
    private final PlaceDiscoveryAlgorithm<?, ?> algorithm;

    public FromLogPlacesProvider(XLog log, PlaceDiscoveryAlgorithm<?, ?> algorithm) {
        this.log = log;
        this.algorithm = algorithm;
    }

    /**
     * Creates a PlacesProvider that discovers places using the specified algorithm.
     * @param log the XLog to discover places from
     * @param algorithmId the algorithm to use for place discovery
     * @return a PlacesProvider that discovers places using the specified algorithm
     */
    public static PlacesProvider getInstance(XLog log, PlaceDiscoveryAlgorithmId algorithmId) {
        if (algorithmId == PlaceDiscoveryAlgorithmId.ESTMiner) {
            return est(log);
        } else if (algorithmId == PlaceDiscoveryAlgorithmId.SPECpp) {
            return specpp(log);
        } else if (algorithmId == PlaceDiscoveryAlgorithmId.HeuristicMiner) {
            return heuristicMiner(log);
        } else if (algorithmId == PlaceDiscoveryAlgorithmId.InductiveMiner) {
            return inductiveMiner(log);
        }
        throw new IllegalArgumentException("Unsupported PlaceDiscoveryAlgorithmId: " + algorithmId);
    }

    @Override
    public Set<Place> provide() {
        return algorithm.getPlaces(this.log).getPlaces();
    }

    /**
     * Creates a PlacesProvider that discovers places using the recommended algorithm (SPECpp).
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places using the recommended algorithm
     */
    public static PlacesProvider recommended(XLog log) {
        return specpp(log);
    }

    /**
     * Creates a PlacesProvider that discovers places using the EST-Miner algorithm.
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places using the EST-Miner algorithm
     */
    public static PlacesProvider est(XLog log) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        EstMinerPlaceDiscoveryParameters parameters = new EstMinerPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
    }

    /**
     * Creates a PlacesProvider that discovers places using the SPECpp algorithm.
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places using the SPECpp algorithm
     */
    public static PlacesProvider specpp(XLog log) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        SPECppPlaceDiscoveryParameters parameters = new SPECppPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
    }

    /**
     * Creates a PlacesProvider that discovers places using the Heuristic Miner algorithm.
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places using the Heuristic Miner algorithm
     */
    public static PlacesProvider heuristicMiner(XLog log) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        HeuristicMinerPlaceDiscoveryParameters parameters = new HeuristicMinerPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
    }

    /**
     * Creates a PlacesProvider that discovers places using the Inductive Miner algorithm.
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places using the Inductive Miner algorithm
     */
    public static PlacesProvider inductiveMiner(XLog log) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        InductiveMinerPlaceDiscoveryParameters parameters = new InductiveMinerPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
    }
}
