package org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Set;

public class FPGrowthForPlacesLPMBuildingInput implements LPMBuildingInput {

    private EventLog log;
    private Set<Place> places;

    public FPGrowthForPlacesLPMBuildingInput(EventLog log, Set<Place> places) {
        this.log = log;
        this.places = places;
    }

    public EventLog getLog() {
        return this.log;
    }

    public Set<Place> getPlaces() {
        return this.places;
    }

    @Override
    public EventLog getEventLog() {
        return this.log;
    }
}
