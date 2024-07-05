package org.processmining.placebasedlpmdiscovery.view.model.lpmdistances;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;

import java.util.HashMap;
import java.util.Map;

public class MixedModelDistanceVM implements org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM {

    private final Map<String, Double> weights;
//    private final Map<String, ModelDistanceVM> distances;
    private final Map<String, Map<String, Object>> distances;


    public MixedModelDistanceVM() {
        this.weights = new HashMap<>();
        this.distances = new HashMap<>();
    }

    public void addDistance(String name, Double weight, Map<String, Object> modelDistanceConfig) {
        this.weights.put(name, weight);
//        this.distances.put(name, distance);
        this.distances.put(name, modelDistanceConfig);
    }

    public void removeDistance(String key) {
        this.weights.remove(key);
        this.distances.remove(key);
    }
}
