package org.processmining.lpms.model.petrinets.nodes;

public class DefaultArc implements Arc {

    private final Node source;
    private final Node target;

    public DefaultArc(Node source, Node target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Node getSource() {
        return source;
    }

    @Override
    public Node getTarget() {
        return target;
    }

}
