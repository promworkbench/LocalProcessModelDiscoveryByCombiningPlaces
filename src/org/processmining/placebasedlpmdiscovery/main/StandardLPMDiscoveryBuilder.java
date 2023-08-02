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

public class StandardLPMDiscoveryBuilder implements LPMDiscoveryBuilder {

    @Override
    public LPMDiscoveryAlg build() {
        return null;
    }

    @Override
    public void setPlaceDiscovery(PlaceDiscovery placeDiscovery) {

    }

    @Override
    public void setPlaceChooser(PlaceChooser placeChooser) {

    }

    @Override
    public void setLPMCombination() {

    }

    @Override
    public void setRunningContext(RunningContext runningContext) {

    }

    @Override
    public void registerLPMWindowEvaluator(String name, WindowLPMEvaluator<? extends LPMEvaluationResult> windowEvaluator) {
        
    }

    @Override
    public void setEvaluationController(LPMEvaluationController evaluationController) {

    }

    @Override
    public void setFiltrationController(LPMFiltrationController filtrationController) {

    }

    @Override
    public void setCombinationGuard(AndCombinationGuard andCombinationGuard) {

    }

    @Override
    public void registerLPMFilter(LPMFilter filter, boolean b) {

    }
}
