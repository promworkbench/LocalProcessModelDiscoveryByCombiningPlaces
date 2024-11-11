package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlgFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.LPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.StandardLPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StandardLPMDiscoveryBuilder implements LPMDiscoveryBuilder {

    // objects that the builder can create
    private LPMEvaluationController evaluationController;
    private LPMFiltrationController filtrationController;

    private LPMBuildingAlgFactory lpmBuildingAlgFactory;

    // objects that are given to the builder
    private RunningContext runningContext;

    private PlaceBasedLPMDiscoveryParameters parameters;

    private Map<String, WindowLPMCollector<?>> windowEvaluators;
    private Collection<LPMFilter> lpmFilters;

    public StandardLPMDiscoveryBuilder() {
        this.evaluationController = new LPMEvaluationController();
        this.filtrationController = new LPMFiltrationController(evaluationController);
        this.lpmBuildingAlgFactory = new LPMBuildingAlgFactory(this.evaluationController);

        this.windowEvaluators = new HashMap<>();
        this.lpmFilters = new ArrayList<>();
    }

    @Override
    public LPMDiscoveryAlg build() {
        // register evaluators
        windowEvaluators.forEach((key, value) -> this.evaluationController.registerEvaluator(key, value));
        lpmFilters.forEach(filter -> this.filtrationController.addLPMFilter(filter, filter.needsEvaluation()));

        StandardLPMDiscoveryAlg alg = new StandardLPMDiscoveryAlg(
                this.runningContext,
                this.parameters,
                this.lpmBuildingAlgFactory.createLPMBuildingAlg(this.parameters.getLpmBuildingAlgType()),
                this.filtrationController);

        return alg;
    }

    @Override
    public void reset() {
        this.runningContext = null;
        this.evaluationController = null;
        this.filtrationController = null;
        this.windowEvaluators = new HashMap<>();
        this.lpmFilters = new ArrayList<>();
    }

    @Override
    public void setRunningContext(RunningContext runningContext) {
        this.runningContext = runningContext;
    }

    @Override
    public void registerLPMWindowCollector(String name,
                                           WindowLPMCollector<? extends LPMCollectorResult> windowCollector) {
        if (this.windowEvaluators.containsKey(name)) {
            throw new IllegalArgumentException("An evaluator with the same name is already existing.");
        }
        this.windowEvaluators.put(name, windowCollector);
    }

    @Override
    public void registerLPMFilter(LPMFilter filter, boolean b) {
        this.lpmFilters.add(filter);
    }

    @Override
    public void setParameters(PlaceBasedLPMDiscoveryParameters parameters) {
        this.parameters = parameters;
    }
}
