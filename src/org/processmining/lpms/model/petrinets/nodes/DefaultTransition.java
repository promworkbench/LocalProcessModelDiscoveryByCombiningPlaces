package org.processmining.lpms.model.petrinets.nodes;

import java.util.Objects;
import java.util.UUID;

public class DefaultTransition implements Transition {

    private final UUID id;
    private final String label;

    public DefaultTransition(String label) {
        this.label = label;
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DefaultTransition that = (DefaultTransition) o;
        return Objects.equals(id, that.id) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
