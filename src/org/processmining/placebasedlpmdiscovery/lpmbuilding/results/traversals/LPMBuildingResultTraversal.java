package org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public interface LPMBuildingResultTraversal {

    boolean hasNext();

    LocalProcessModel next();
}
