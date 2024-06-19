package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

public class MixedModelDistanceAddDistanceEmittableDataVM implements EmittableDataVM {
    private String name;
    private double weight;
    private ModelDistanceVM model;

    public MixedModelDistanceAddDistanceEmittableDataVM(String name, double weight, ModelDistanceVM model) {
        this.name = name;
        this.weight = weight;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public ModelDistanceVM getModel() {
        return model;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.MixedModelDistanceAddDistanceVM;
    }
}
