package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.configurator;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

public class FromParametersLPMDAlgBuilderConfigurator implements LPMDAlgBuilderConfigurator {

    private final PlaceBasedLPMDiscoveryPluginParameters parameters;
    public FromParametersLPMDAlgBuilderConfigurator(PlaceBasedLPMDiscoveryPluginParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void configure(LPMDiscoveryAlgBuilder builder) {
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
            builder.registerLPMFilter(filter);
        }
    }
}
