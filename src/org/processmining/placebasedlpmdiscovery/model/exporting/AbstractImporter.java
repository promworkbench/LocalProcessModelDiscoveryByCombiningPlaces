package org.processmining.placebasedlpmdiscovery.model.exporting;

import java.io.File;
import java.io.InputStream;

public abstract class AbstractImporter<T> implements Importer<T> {

    protected final InputStream is;
    protected final Class<T> tClass;

    public AbstractImporter(InputStream is, Class<T> tClass) {
        this.is = is;
        this.tClass = tClass;
    }
}
