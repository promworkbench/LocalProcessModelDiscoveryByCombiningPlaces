package org.processmining.placebasedlpmdiscovery.model.exporting;

public interface Exportable<T> {

    void export(Exporter<T> exporter);
}
