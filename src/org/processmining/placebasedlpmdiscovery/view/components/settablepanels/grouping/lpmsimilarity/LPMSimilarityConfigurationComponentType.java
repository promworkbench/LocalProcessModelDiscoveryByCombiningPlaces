package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentType;

public enum LPMSimilarityConfigurationComponentType implements ConfigurationComponentType {
    DataAttributeLPMSimilarityConfigurationComponent,
    MixedLPMSimilarityConfigurationComponent,
    ModelSimilarityLPMSimilarityConfigurationComponent;

    public static LPMSimilarityConfigurationComponentType getEnum(String value) {
        switch (value) {
            case "Mixed":
                return LPMSimilarityConfigurationComponentType.MixedLPMSimilarityConfigurationComponent;
            case "Model Similarity":
                return LPMSimilarityConfigurationComponentType.ModelSimilarityLPMSimilarityConfigurationComponent;
            case "Data Attributes":
                return LPMSimilarityConfigurationComponentType.DataAttributeLPMSimilarityConfigurationComponent;
        }
        throw new IllegalArgumentException("No enum for value " + value);
    }
}
