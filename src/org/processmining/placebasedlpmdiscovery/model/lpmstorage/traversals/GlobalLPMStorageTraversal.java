package org.processmining.placebasedlpmdiscovery.model.lpmstorage.traversals;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public interface GlobalLPMStorageTraversal {

    boolean hasNext();

    LocalProcessModel next();

}
