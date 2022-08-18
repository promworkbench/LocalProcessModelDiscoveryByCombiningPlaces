package org.processmining.placebasedlpmdiscovery.model.exporting;

import java.io.File;

public abstract class AbstractExporter<T> implements Exporter<T>{

    protected final File file;

    public AbstractExporter(File file){
        this.file = file;
    }

    @Override
    public abstract void export(T object);
}
