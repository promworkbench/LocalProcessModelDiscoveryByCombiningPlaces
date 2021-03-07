package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphNode;

import java.util.Objects;


public class FPGrowthPlaceFollowGraphNode extends AbstractDirectedGraphNode {

    private FPGrowthPlaceFollowGraph containmentGraph;
    private Place place;

//    private Set<FPGrowthPlaceFollowGraphEdge> inEdges;
//    private Set<FPGrowthPlaceFollowGraphEdge> outEdges;

    public FPGrowthPlaceFollowGraphNode(Place place, FPGrowthPlaceFollowGraph containmentGraph) {
        this.place = place;
        this.containmentGraph = containmentGraph;
    }

    public boolean isPlace(Place place) {
        return this.place.equals(place);
    }

    public Place getPlace() {
        return this.place;
    }

    @Override
    public AbstractDirectedGraph<?, ?> getGraph() {
        return this.containmentGraph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FPGrowthPlaceFollowGraphNode node = (FPGrowthPlaceFollowGraphNode) o;
        return Objects.equals(place, node.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), place);
    }
}
