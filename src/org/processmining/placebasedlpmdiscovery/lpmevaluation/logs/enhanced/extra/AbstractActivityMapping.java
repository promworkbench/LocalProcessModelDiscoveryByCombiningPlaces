package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra;

import java.util.*;

public abstract class AbstractActivityMapping<T> {
    protected final Map<String, T> labelMap;
    protected final Map<T, String> reverseLabelMap;
    protected final Set<T> invisible;

    public AbstractActivityMapping() {
        invisible = new HashSet<>();
        labelMap = new HashMap<>();
        reverseLabelMap = new HashMap<>();
    }

    public void addInvisibleTransitionsInLabelMap(Set<String> transitionLabels) {
        for (String label : transitionLabels) {
            if (this.labelMap.containsKey(label))
                continue;
            this.mapInvisible(label);
        }
    }

    public Map<String, T> getLabelMap() {
        return labelMap;
    }

    public Map<T, String> getReverseLabelMap() {
        return reverseLabelMap;
    }

    public Set<T> getInvisible() {
        return this.invisible;
    }

    public T getMapping(String label) {
        // if the label is not in the map create the mapping and return it
        if (!this.labelMap.containsKey(label)) {
            return this.map(label);
        }
        return this.labelMap.get(label);
    }

    protected abstract T map(String label);

    public T mapInvisible(String label) {
        T mapping = this.map(label);
        this.invisible.add(mapping);
        return mapping;
    }
}
