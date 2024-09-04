package org.processmining.placebasedlpmdiscovery.model.discovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Set;

public class FPGrowthForPlacesLPMBuildingInput implements LPMBuildingInput {

    private EventLog log;
    private Set<Place> places;
    private LPMCombinationParameters parameters; // TODO: Should be FPGrowthCombinationParameters 30.08.2024

    public EventLog getLog() {
        return this.log;
    }

    public Set<Place> getPlaces() {
        return this.places;
    }

    public LPMCombinationParameters getCombinationParameters() {
        return parameters;
    }
}
