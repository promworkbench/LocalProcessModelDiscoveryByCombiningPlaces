package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MixedLPMSimilarityViewConfiguration extends LPMSimilarityViewConfiguration {

    private final Map<String, Double> weights;
    private final Map<String, LPMSimilarityViewConfiguration> configs;

    public MixedLPMSimilarityViewConfiguration() {
        super("Mixed", new HashMap<>());
        this.weights = new HashMap<>();
        this.configs = new HashMap<>();
    }

    public MixedLPMSimilarityViewConfiguration(Map<String, Double> weights,
                                               Map<String, LPMSimilarityViewConfiguration> configs) {
        super("Mixed", Collections.emptyMap());
        this.weights = weights;
        this.configs = configs;
    }

    public void addDistance(String name, Double weight, LPMSimilarityViewConfiguration configuration) {
        this.weights.put(name, weight);
        this.configs.put(name, configuration);
    }

    public void removeDistance(String key) {
        this.weights.remove(key);
        this.configs.remove(key);
    }

    @Override
    public Map<String, Object> getMap() {
        this.parameterConfiguration.put("weights", this.weights);
        this.parameterConfiguration.put("configs", this.configs
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getMap())));
        return super.getMap();
    }
}
