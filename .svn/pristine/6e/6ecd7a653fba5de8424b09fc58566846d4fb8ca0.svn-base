package org.processmining.placebasedlpmdiscovery.model.serializable;

import java.io.Serializable;
import java.util.Collection;

public abstract class SerializableCollection<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1493304739663755466L;
    private T type;

    public abstract boolean add(T element);

    public abstract boolean contains(T element);

    public abstract boolean remove(T element);

    public abstract int size();

    public abstract Collection<T> getElements();

    public abstract boolean addAll(Collection<T> elements);

    public Class<?> getElementClass() {
        return this.type.getClass();
    }
}
