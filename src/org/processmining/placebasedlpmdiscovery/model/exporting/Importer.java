package org.processmining.placebasedlpmdiscovery.model.exporting;

import java.io.InputStream;

public interface Importer<T> {

    T read(InputStream in);
}
