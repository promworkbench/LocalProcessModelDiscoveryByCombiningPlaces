package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlgFactory;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlgType;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.configurator.LPMDAlgBuilderConfigurator;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.LPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.StandardLPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LPMDiscoveryAlgBuilderImpl implements LPMDiscoveryAlgBuilder {

    // objects that the builder can create
    private final LPMEvaluationController evaluationController;
    private final LPMFiltrationController filtrationController;
    private final LPMBuildingAlgFactory lpmBuildingAlgFactory;

    // objects that are given to the builder
    private PlaceBasedLPMDiscoveryPluginParameters parameters;
    private final Map<String, WindowLPMCollector<?>> windowEvaluators;
    private final Collection<LPMFilter> lpmFilters;
    private LPMBuildingAlgType lpmBuildingAlgType;

    public LPMDiscoveryAlgBuilderImpl() {
        // create helper classes
        this.evaluationController = new LPMEvaluationController();
        this.filtrationController = new LPMFiltrationController(evaluationController);
        this.lpmBuildingAlgFactory = new LPMBuildingAlgFactory(this.evaluationController);

        // set defaults
        this.lpmBuildingAlgType = LPMBuildingAlgType.FPGrowthForPlaces;
        this.windowEvaluators = new HashMap<>();
        this.lpmFilters = new ArrayList<>();
    }

    @Override
    public LPMDiscoveryAlg build() {
        // register evaluators
        windowEvaluators.forEach(this.evaluationController::registerEvaluator);
        lpmFilters.forEach(filter -> this.filtrationController.addLPMFilter(filter, filter.needsEvaluation()));

        StandardLPMDiscoveryAlg alg = new StandardLPMDiscoveryAlg(
                this.parameters,
                this.lpmBuildingAlgFactory.createLPMBuildingAlg(this.lpmBuildingAlgType),
                this.filtrationController);

        return alg;
    }

    @Override
    public LPMDiscoveryAlgBuilder reset() {
        return new LPMDiscoveryAlgBuilderImpl();
    }

    @Override
    public LPMDiscoveryAlgBuilder configureWithConfigurator(LPMDAlgBuilderConfigurator configurator) {
        configurator.configure(this);
        return this;
    }

    @Override
    public LPMDiscoveryAlgBuilder setLPMBuildingAlg(LPMBuildingAlgType lpmBuildingAlgType) {
        this.lpmBuildingAlgType = lpmBuildingAlgType;
        return this;
    }

    @Override
    public LPMDiscoveryAlgBuilder registerLPMWindowCollector(String name,
                                           WindowLPMCollector<? extends LPMCollectorResult> windowCollector) {
        if (this.windowEvaluators.containsKey(name)) {
            throw new IllegalArgumentException("An evaluator with the same name is already existing.");
        }
        this.windowEvaluators.put(name, windowCollector);
        return this;
    }

    @Override
    public LPMDiscoveryAlgBuilder registerLPMFilter(LPMFilter filter) {
        this.lpmFilters.add(filter);
        return this;
    }

    @Override
    public void setParameters(PlaceBasedLPMDiscoveryPluginParameters parameters) {
        this.parameters = parameters;
    }

}
