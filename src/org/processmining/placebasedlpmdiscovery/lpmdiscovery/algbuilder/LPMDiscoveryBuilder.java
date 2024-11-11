package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.LPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public interface LPMDiscoveryBuilder {

    LPMDiscoveryAlg build();

    void reset();

    void setRunningContext(RunningContext runningContext);

    void registerLPMWindowCollector(String name, WindowLPMCollector<? extends LPMCollectorResult> windowCollector);

    void registerLPMFilter(LPMFilter filter, boolean b);

    void setParameters(PlaceBasedLPMDiscoveryParameters parameters);
}
