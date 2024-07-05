package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents;

import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.LPMSimilarityConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.LPMSimilaritySetupComponent;

public interface ConfigurationComponentFactory {

    ConfigurationComponent create(ConfigurationComponentType type);

    LPMSimilaritySetupComponent create(LPMSimilarityConfigurationComponentType type);
}
