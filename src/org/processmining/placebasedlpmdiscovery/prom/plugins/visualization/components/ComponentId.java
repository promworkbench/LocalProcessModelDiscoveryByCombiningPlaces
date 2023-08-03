package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import java.util.UUID;

public class ComponentId {

    public enum Type {
        Empty,
        TableComponent,
        LPMVisualizer,
        LogStatistics,
        BasicLPMEvalMetrics
    }

    private final Type type;
    private final UUID id;

    public ComponentId (Type type) {
        this.type = type;
        this.id = UUID.randomUUID();
    }

    public Type getType() {
        return type;
    }

    public UUID getId() {
        return id;
    }
}
