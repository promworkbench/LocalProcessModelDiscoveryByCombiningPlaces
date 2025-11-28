package org.processmining.lpms.model.petrinets.nodes;

import java.util.UUID;

public class DefaultPlace implements Place {
    private final UUID id;

    public DefaultPlace() {
        this.id = UUID.randomUUID();
    }

    public DefaultPlace(UUID id) {
        this.id = id;
    }
}
