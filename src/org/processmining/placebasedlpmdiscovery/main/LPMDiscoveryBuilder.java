package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.AndCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;

public interface LPMDiscoveryBuilder {

    LPMDiscoveryAlg build();

    void setPlaceDiscovery(PlaceDiscovery placeDiscovery);

    void setPlaceChooser(PlaceChooser placeChooser);

    void setLPMCombination();

    void setRunningContext(RunningContext runningContext);

    void registerLPMWindowEvaluator(String name, WindowLPMEvaluator<? extends LPMEvaluationResult> windowEvaluator);

    void setEvaluationController(LPMEvaluationController evaluationController);

    void setFiltrationController(LPMFiltrationController filtrationController);

    void setCombinationGuard(AndCombinationGuard andCombinationGuard);

    void registerLPMFilter(LPMFilter filter, boolean b);
}
