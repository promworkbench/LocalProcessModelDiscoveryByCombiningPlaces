package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.Exportable;

import java.util.Collection;

public interface LPMDiscoveryResult extends Exportable<LPMDiscoveryResult> {
    Collection<LocalProcessModel> getAllLPMs();
}
