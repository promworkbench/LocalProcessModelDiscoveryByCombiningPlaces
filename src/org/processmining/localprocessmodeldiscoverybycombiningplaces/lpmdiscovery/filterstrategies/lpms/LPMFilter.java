package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

import java.util.Set;

/**
 * Interface that needs to be implemented by all LPMs filters
 */
public interface LPMFilter {

    Set<LocalProcessModel> filter(Set<LocalProcessModel> lpms);

    boolean shouldKeep(LocalProcessModel lpm);

    int getPriority();

    boolean needsEvaluation();
}
