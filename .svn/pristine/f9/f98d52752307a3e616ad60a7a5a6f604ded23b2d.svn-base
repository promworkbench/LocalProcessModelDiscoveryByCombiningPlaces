package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.AbovePassageCoverageThresholdLPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.AbovePassageRepetitionThresholdLPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;

public class LPMFilterFactory {

    private LPMFilterParameters parameters;

    public LPMFilterFactory(LPMFilterParameters parameters) {
        this.parameters = parameters;
    }

    public LPMFilter getLPMFilter(LPMFilterId filterId) {
        if (filterId == LPMFilterId.AbovePassageCoverageThresholdLPMFilter)
            return new AbovePassageCoverageThresholdLPMFilter(parameters.getAbovePassageCoverageThreshold());
//        if (filterId == LPMFilterId.AboveTransitionOverlappingThresholdLPMFilter)
//            return new AboveTransitionOverlappingThresholdLPMFilter(parameters.getAboveTransitionOverlappingThreshold());
        if (filterId == LPMFilterId.AbovePassageRepetitionThresholdLPMFilter)
            return new AbovePassageRepetitionThresholdLPMFilter(parameters.getAbovePassageRepetitionThreshold());
        throw new IllegalArgumentException("There is no lpm filter with that id");
    }
}
