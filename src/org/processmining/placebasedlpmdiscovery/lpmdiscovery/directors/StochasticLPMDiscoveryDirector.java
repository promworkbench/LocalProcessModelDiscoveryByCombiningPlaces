package org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;

public class StochasticLPMDiscoveryDirector extends AbstractLPMDiscoveryDirector {

    private final LPMCollectorFactory lpmCollectorFactory;

    public StochasticLPMDiscoveryDirector(LPMDiscoveryBuilder builder,
                                          LPMCollectorFactory lpmCollectorFactory) {
        super(builder);
        this.lpmCollectorFactory = lpmCollectorFactory;
    }

    @Override
    public void make() {
        builder.registerLPMWindowCollector(
                StandardLPMCollectorId.PassageVisitsCollector.name(),
                this.lpmCollectorFactory.getWindowEvaluator(StandardLPMCollectorId.PassageVisitsCollector));
    }
}
