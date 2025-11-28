package org.processmining.lpms.model.petrinets.nodes;

import java.util.Objects;
import java.util.UUID;

public class DefaultTransition implements Transition {

    private final UUID id;
    private final String label;
    private final boolean visible;

    DefaultTransition() {
        this.label = null;
        this.id = UUID.randomUUID();
        this.visible = false;
    }

    DefaultTransition(String label) {
        this.label = label;
        this.id = UUID.randomUUID();
        this.visible = true;
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

    @Override
    public boolean isVisible() {
        return this.visible;
    }
}
