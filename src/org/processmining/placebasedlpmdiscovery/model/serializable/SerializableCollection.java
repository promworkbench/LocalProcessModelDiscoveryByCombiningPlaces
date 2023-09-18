package org.processmining.placebasedlpmdiscovery.model.serializable;

import java.io.Serializable;
import java.util.Collection;

public interface SerializableCollection<T extends Serializable> extends Serializable {

    boolean add(T element);

    boolean contains(T element);

    boolean remove(T element);

    int size();

    Collection<T> getElements();

    boolean addAll(Collection<T> elements);
}
