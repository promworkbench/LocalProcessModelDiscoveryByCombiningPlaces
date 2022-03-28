package org.processmining.placebasedlpmdiscovery.model.exporting;

import java.io.File;

public interface Exportable<T> {

    void export(Exporter<T> exporter);
}
