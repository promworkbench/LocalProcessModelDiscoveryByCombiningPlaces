package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;

public interface LPMDiscoveryBuilder {

    LPMDiscoveryAlg build();

    void setPlaceDiscovery(PlaceDiscovery placeDiscovery);

    void setPlaceChooser(PlaceChooser placeChooser);

    void setLPMCombination();

    void registerLPMFilter();

    void registerLPMEvaluator();

    void setRunningContext(RunningContext runningContext);
}
