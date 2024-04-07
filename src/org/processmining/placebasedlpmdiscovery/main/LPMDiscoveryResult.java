package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.Exportable;

import java.util.Collection;

public interface LPMDiscoveryResult extends Exportable<LPMDiscoveryResult> {
    Collection<LocalProcessModel> getAllLPMs();

    /**
     * Returns the input for which this LPMDiscoveryResult was computed. This is needed so that postprocessing steps
     * and advanced filtration strategies or groupings could be executed.
     */
    LPMDiscoveryInput getInput();

    /**
     * Sets the input for which this LPMDiscoveryResult was computed. This is needed so that postprocessing steps
     * and advanced filtration strategies or groupings could be executed.
     */
    void setInput(LPMDiscoveryInput input);
    
    LPMDiscoveryConfig getConfig();

    void keep(int lpmCount);
}
