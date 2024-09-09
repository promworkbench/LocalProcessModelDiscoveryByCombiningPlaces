package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.LPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public interface LPMDiscoveryBuilder {

    LPMDiscoveryAlg build();

    void reset();

    void setPlaceDiscovery(PlaceDiscovery placeDiscovery);

    void setPlaceChooser(PlaceChooser placeChooser);

    void setLPMCombination(LPMCombinationController lpmCombination);

    void setRunningContext(RunningContext runningContext);

    void registerLPMWindowCollector(String name, WindowLPMCollector<? extends LPMCollectorResult> windowCollector);

    void setCombinationGuard(CombinationGuard combinationGuard);

    void registerLPMFilter(LPMFilter filter, boolean b);

    void setParameters(PlaceBasedLPMDiscoveryParameters parameters);
}
