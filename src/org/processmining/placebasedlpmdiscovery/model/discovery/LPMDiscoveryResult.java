package org.processmining.placebasedlpmdiscovery.model.discovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryConfig;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.Exportable;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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

    static LPMDiscoveryResult fromFile(String filePath) throws IOException {
        return new FromFileLPMDiscoveryResult(filePath);
    }

    void addAdditionalResults(String key, Object additionalResult);

    Map<String, Object> getAdditionalResults();
}
