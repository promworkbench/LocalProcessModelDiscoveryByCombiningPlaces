package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.model.exporting.Exportable;

public interface LPMDiscoveryResult extends Exportable<LPMDiscoveryResult> {
    Collection<LocalProcessModel> getAllLPMs();
}
