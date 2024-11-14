package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlgType;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.configurator.LPMDAlgBuilderConfigurator;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.LPMDiscoveryAlg;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public interface LPMDiscoveryAlgBuilder {

    LPMDiscoveryAlg build();

    LPMDiscoveryAlgBuilder reset();

    LPMDiscoveryAlgBuilder registerLPMWindowCollector(String name, WindowLPMCollector<? extends LPMCollectorResult> windowCollector);

    LPMDiscoveryAlgBuilder registerLPMFilter(LPMFilter filter);

    void setParameters(PlaceBasedLPMDiscoveryParameters parameters);

    LPMDiscoveryAlgBuilder configureWithConfigurator(LPMDAlgBuilderConfigurator configurator);

    LPMDiscoveryAlgBuilder setLPMBuildingAlg(LPMBuildingAlgType lpmBuildingAlg);

}
