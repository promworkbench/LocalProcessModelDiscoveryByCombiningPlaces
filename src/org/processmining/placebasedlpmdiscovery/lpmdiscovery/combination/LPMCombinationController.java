package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public interface LPMCombinationController {

    void setGuard(CombinationGuard guard);

    Set<LocalProcessModel> combine(Set<Place> places, XLog log, int count);
}
