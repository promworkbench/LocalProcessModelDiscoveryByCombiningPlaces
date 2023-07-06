package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import java.io.InputStream;

public interface Importer<T> {

    T read(Class<T> tClass, InputStream is);
}
