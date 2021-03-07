package org.processmining.placebasedlpmdiscovery.placediscovery.parameters;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.settings.HeuristicsMinerSettings;

public class HeuristicMinerPlaceDiscoveryParameters extends PlaceDiscoveryParameters {

    private HeuristicsMinerSettings settings;

    public HeuristicMinerPlaceDiscoveryParameters() {
        this.settings = new HeuristicsMinerSettings();
        this.settings.setClassifier(new XEventNameClassifier());
    }

    @Override
    public PlaceDiscoveryAlgorithm<
                ? extends PlaceDiscoveryParameters, ?> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }

    public HeuristicsMinerSettings getSettings() {
        return settings;
    }

    public void setSettings(HeuristicsMinerSettings settings) {
        this.settings = settings;
    }
}
