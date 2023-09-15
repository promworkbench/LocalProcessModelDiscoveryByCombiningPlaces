package org.processmining.placebasedlpmdiscovery.model.exporting.instancecreators;

import com.google.gson.InstanceCreator;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;

public class PairInstanceCreator implements InstanceCreator<Pair<?,?>> {
    @Override
    public Pair<?,?> createInstance(Type type) {
        return new MutablePair<>(new Object(), new Object());
    }
}
