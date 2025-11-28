package org.processmining.lpms.model.petrinets.nodes;

public interface Arc {

    Node getSource();

    Node getTarget();

    static Arc of(Node source, Node target) {
        return new DefaultArc(source, target);
    }
}
