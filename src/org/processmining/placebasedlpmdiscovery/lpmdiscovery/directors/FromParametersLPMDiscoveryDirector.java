package org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class FromParametersLPMDiscoveryDirector extends AbstractLPMDiscoveryDirector {
    private final PlaceBasedLPMDiscoveryParameters parameters;

    public FromParametersLPMDiscoveryDirector(LPMDiscoveryBuilder builder,
                                              PlaceBasedLPMDiscoveryParameters parameters) {
        super(builder);
        this.parameters = parameters;
    }

    @Override
    public void make() {
        builder.setParameters(this.parameters);

        // add evaluators
        LPMCollectorFactory evaluatorFactory = new LPMCollectorFactory();
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.PassageCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.PassageCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.FittingWindowEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.FittingWindowEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.TransitionCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.TransitionCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.TraceSupportCountEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.TraceSupportCountEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.EventCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.EventCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMCollectorId.EventAttributeCollector.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMCollectorId.EventAttributeCollector));

        // set filters
        LPMFilterParameters filterParameters = parameters.getLpmFilterParameters();
        LPMFilterFactory filterFactory = new LPMFilterFactory(filterParameters);
        for (LPMFilterId filterId : filterParameters.getLPMFilterIds()) {
            LPMFilter filter = filterFactory.getLPMFilter(filterId);
            builder.registerLPMFilter(filter, filter.needsEvaluation());
        }
    }
}
