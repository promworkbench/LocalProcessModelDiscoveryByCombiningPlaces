package org.processmining.placebasedlpmdiscovery.placediscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public class StandardPlaceDiscoveryResult implements PlaceDiscoveryResult {

    private Set<Place> places;
    private XLog log;

    public StandardPlaceDiscoveryResult() {

    }
    public StandardPlaceDiscoveryResult(Set<Place> places) {
        this.places = places;
    }

    @Override
    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }

    public XLog getLog() {
        return log;
    }

    public void setLog(XLog log) {
        this.log = log;
    }
}
