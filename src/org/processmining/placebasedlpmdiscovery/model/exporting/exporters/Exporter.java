package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import java.io.OutputStream;

public interface Exporter<T> {

    void export(T object, OutputStream os);
}
