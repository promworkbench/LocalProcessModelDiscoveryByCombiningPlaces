package org.processmining.placebasedlpmdiscovery.model.exporting;

import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;

import java.io.OutputStream;

public interface Exportable<T> {

    void export(Exporter<T> exporter, OutputStream os);
}
