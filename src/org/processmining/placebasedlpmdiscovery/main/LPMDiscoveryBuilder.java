package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public interface LPMDiscoveryBuilder {

    LPMDiscoveryAlg build();

    void reset();

    void setPlaceDiscovery(PlaceDiscovery placeDiscovery);

    void setPlaceChooser(PlaceChooser placeChooser);

    void setLPMCombination(LPMCombinationController lpmCombination);

    void setRunningContext(RunningContext runningContext);

    void registerLPMWindowEvaluator(String name, WindowLPMEvaluator<? extends LPMEvaluationResult> windowEvaluator);

    void setEvaluationController(LPMEvaluationController evaluationController);

    void setFiltrationController(LPMFiltrationController filtrationController);

    void setCombinationGuard(CombinationGuard combinationGuard);

    void registerLPMFilter(LPMFilter filter, boolean b);

    void setParameters(PlaceBasedLPMDiscoveryParameters parameters);
}
