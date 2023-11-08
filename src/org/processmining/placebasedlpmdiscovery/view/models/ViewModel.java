package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public interface ViewModel {

    Collection<LocalProcessModel> getLPMs();

    void setLPMs(Collection<LocalProcessModel> lpms);
}
