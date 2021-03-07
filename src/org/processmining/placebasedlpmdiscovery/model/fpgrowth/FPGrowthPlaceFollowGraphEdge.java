package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.models.graphbased.directed.AbstractDirectedGraphEdge;

import java.util.Objects;

public class FPGrowthPlaceFollowGraphEdge extends AbstractDirectedGraphEdge<FPGrowthPlaceFollowGraphNode, FPGrowthPlaceFollowGraphNode> {

    private FPGrowthPlaceFollowGraphNode source;
    private FPGrowthPlaceFollowGraphNode target;
    private double weight;

    public FPGrowthPlaceFollowGraphEdge(FPGrowthPlaceFollowGraphNode source, FPGrowthPlaceFollowGraphNode target, double weight) {
        super(source, target);
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public void addWeight(double weight) {
        this.weight += weight;
    }

    public FPGrowthPlaceFollowGraphNode getSource() {
        return source;
    }

    public FPGrowthPlaceFollowGraphNode getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FPGrowthPlaceFollowGraphEdge edge = (FPGrowthPlaceFollowGraphEdge) o;
        return Double.compare(edge.weight, weight) == 0 &&
                Objects.equals(source, edge.source) &&
                Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, target, weight);
    }
}
