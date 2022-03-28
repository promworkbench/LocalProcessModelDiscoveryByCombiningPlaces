package org.processmining.placebasedlpmdiscovery.model.exporting;

public interface Exporter<T> {

    void export(T object);
}
