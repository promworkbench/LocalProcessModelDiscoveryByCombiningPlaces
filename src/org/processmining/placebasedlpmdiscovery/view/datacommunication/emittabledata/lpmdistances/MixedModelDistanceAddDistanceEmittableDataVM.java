package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

import java.util.Map;

public class MixedModelDistanceAddDistanceEmittableDataVM implements EmittableDataVM {
    private String name;
    private double weight;
    private Map<String, Object> modelDistanceConfig;

    public MixedModelDistanceAddDistanceEmittableDataVM(String name, double weight,
                                                        Map<String, Object> modelDistanceConfig) {
        this.name = name;
        this.weight = weight;
        this.modelDistanceConfig = modelDistanceConfig;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public Map<String, Object> getModelDistanceConfig() {
        return modelDistanceConfig;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.MixedModelDistanceAddDistanceVM;
    }

    @Override
    public String getTopic() {
        return this.getType().name();
    }
}
