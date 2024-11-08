package org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Set;

public interface LPMBuildingResultTraversal {

    boolean hasNext();

    LocalProcessModel next();

}
