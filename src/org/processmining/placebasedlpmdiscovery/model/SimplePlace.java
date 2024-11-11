package org.processmining.placebasedlpmdiscovery.model;

import java.util.Objects;
import java.util.Set;

public class SimplePlace<LABEL_TYPE> {

    private final Set<LABEL_TYPE> inputs;
    private final Set<LABEL_TYPE> outputs;

    public SimplePlace(Set<LABEL_TYPE> inputs, Set<LABEL_TYPE> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Set<LABEL_TYPE> getInputs() {
        return inputs;
    }

    public Set<LABEL_TYPE> getOutputs() {
        return outputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePlace<?> that = (SimplePlace<?>) o;
        return Objects.equals(inputs, that.inputs) && Objects.equals(outputs, that.outputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, outputs);
    }
}
