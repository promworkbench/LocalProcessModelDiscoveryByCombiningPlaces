package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public interface LPMDiscoveryResultViewModel extends ViewModel {
    LocalProcessModel getSelectedLPM();
}
