package org.processmining.placebasedlpmdiscovery.model.exporting.gson;

public interface GsonSerializable {

    default String getClassName() {
        return this.getClass().getName();
    }
}