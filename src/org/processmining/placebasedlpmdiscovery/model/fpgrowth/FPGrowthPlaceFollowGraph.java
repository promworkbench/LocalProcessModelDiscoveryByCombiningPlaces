package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import javafx.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.models.graphbased.directed.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class FPGrowthPlaceFollowGraph extends AbstractDirectedGraph<FPGrowthPlaceFollowGraphNode, FPGrowthPlaceFollowGraphEdge>
        implements Serializable {

    private static final long serialVersionUID = -4172966660281492827L;

    private int placeId;
    private Map<Place, Integer> placeNodeIdMap;
    private Map<Integer, FPGrowthPlaceFollowGraphNode> nodeIdNodeMap;
    private Map<String, FPGrowthPlaceFollowGraphEdge> idEdgeMap;

    public FPGrowthPlaceFollowGraph() {
        this.placeId = 1;
        this.placeNodeIdMap = new HashMap<>();
        this.nodeIdNodeMap = new HashMap<>();
        this.idEdgeMap = new HashMap<>();
    }

    public FPGrowthPlaceFollowGraph(FPGrowthPlaceFollowGraph graph) {
        this.placeId = graph.placeId;
        this.placeNodeIdMap = new HashMap<>();
        this.placeNodeIdMap.putAll(graph.placeNodeIdMap);
        this.nodeIdNodeMap = new HashMap<>();
        this.nodeIdNodeMap.putAll(graph.nodeIdNodeMap);
        this.idEdgeMap = new HashMap<>();
        this.idEdgeMap.putAll(graph.idEdgeMap);
    }

    @Override
    protected AbstractDirectedGraph<FPGrowthPlaceFollowGraphNode, FPGrowthPlaceFollowGraphEdge> getEmptyClone() {
        return null;
    }

    @Override
    protected Map<? extends DirectedGraphElement, ? extends DirectedGraphElement> cloneFrom(DirectedGraph<FPGrowthPlaceFollowGraphNode, FPGrowthPlaceFollowGraphEdge> directedGraph) {
        return null;
    }

    @Override
    public void removeEdge(DirectedGraphEdge directedGraphEdge) {
        this.removeEdge(this.getEdgeId(((FPGrowthPlaceFollowGraphEdge) directedGraphEdge).getSource(),
                ((FPGrowthPlaceFollowGraphEdge) directedGraphEdge).getTarget()));
    }

    @Override
    public void removeNode(DirectedGraphNode directedGraphNode) {
        this.removeNode((FPGrowthPlaceFollowGraphNode) directedGraphNode);
    }

    public Integer getNodeId(Place place) {
        return this.placeNodeIdMap.get(place);
    }

    public String getEdgeId(Place source, Place target) {
        return this.getNodeId(source) + "-" + this.getNodeId(target);
    }

    private boolean hasEdge(FPGrowthPlaceFollowGraphNode source, FPGrowthPlaceFollowGraphNode target) {
        return this.idEdgeMap.containsKey(this.getEdgeId(source, target));
    }

    public String getEdgeId(FPGrowthPlaceFollowGraphNode source, FPGrowthPlaceFollowGraphNode target) {
        return this.getEdgeId(source.getPlace(), target.getPlace());
    }

    public FPGrowthPlaceFollowGraphNode getNode(Place place) {
        int id = this.placeNodeIdMap.getOrDefault(place, 0);
        if (id > 0)
            return this.nodeIdNodeMap.get(id);
        return null;
    }

    public FPGrowthPlaceFollowGraphEdge getEdge(String id) {
        return this.idEdgeMap.get(id);
    }

    public void addEdge(Place source, Place target, Integer weight) {
        // get or create source node
        FPGrowthPlaceFollowGraphNode sourceNode = this.getNode(source);
        if (sourceNode == null) {
            sourceNode = createNode(source);
        }
        // get or create target node
        FPGrowthPlaceFollowGraphNode targetNode = this.getNode(target);
        if (targetNode == null) {
            targetNode = createNode(target);
        }

        // get existing edge and add weight or create new edge
        FPGrowthPlaceFollowGraphEdge edge = this.getEdge(this.getEdgeId(source, target));
        if (edge == null) {
            edge = new FPGrowthPlaceFollowGraphEdge(sourceNode, targetNode, weight);
        } else {
            edge.addWeight(weight);
        }
        this.idEdgeMap.put(this.getEdgeId(source, target), edge);
    }

    private FPGrowthPlaceFollowGraphNode createNode(Place place) {
        FPGrowthPlaceFollowGraphNode node = new FPGrowthPlaceFollowGraphNode(place, this);
        this.nodeIdNodeMap.put(this.placeId, node);
        this.placeNodeIdMap.put(place, this.placeId);
        this.placeId++;
        return node;
    }

    private void discardMyDisconnectedNodes() {
        Set<FPGrowthPlaceFollowGraphNode> disconnectedNodes = new HashSet<>();
        for (FPGrowthPlaceFollowGraphNode node : this.getNodes()) {
            boolean connected = false;
            for (FPGrowthPlaceFollowGraphEdge edge : this.getEdges()) {
                if (edge.getSource().equals(node) || edge.getTarget().equals(node)) {
                    connected = true;
                    break;
                }
            }
            if (!connected)
                disconnectedNodes.add(node);
        }

        for (FPGrowthPlaceFollowGraphNode node : disconnectedNodes)
            this.removeNode(node);
    }

    /**
     * Discards some of the edges in the graph. Only keeps (100 - discardPercentage) percentages of the edges
     *
     * @param discardPercentage : the percentage of edges we want to discard
     * @return new graph in which the edges are discarded
     */
    public FPGrowthPlaceFollowGraph discardEdges(int discardPercentage) {
        FPGrowthPlaceFollowGraph discardedEdgesGraph = new FPGrowthPlaceFollowGraph(this);
        discardedEdgesGraph.discardMyEdges(discardPercentage);
        return discardedEdgesGraph;
    }

    private void discardMyEdges(int discardPercentage) {
        List<String> sortedEdgeKeys = this.idEdgeMap.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(stringFPGrowthPlaceFollowGraphEdgeEntry ->
                        stringFPGrowthPlaceFollowGraphEdgeEntry.getValue().getWeight()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int bound = discardPercentage * sortedEdgeKeys.size() / 100;

        for (int i = 0; i < bound; ++i)
            this.removeEdge(sortedEdgeKeys.get(i));

        this.discardMyDisconnectedNodes();
    }

    /**
     * Discards edges that have lower weight than percentageFromMaxWeight percentage of the maximal weight
     *
     * @param percentageFromMaxWeight: the percentage of the maximal weight that every weight must be above
     */
    public void discardLowWeightEdges(int percentageFromMaxWeight) {
        double maxWeight = this.idEdgeMap.values()
                .stream()
                .map(FPGrowthPlaceFollowGraphEdge::getWeight)
                .max(Double::compareTo)
                .orElse(-1.0);

        if (maxWeight == -1)
            throw new UnsupportedOperationException("There are no edges in the graph. The default -1 weight is returned");

        this.idEdgeMap = this.idEdgeMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getWeight() >= percentageFromMaxWeight * maxWeight / 100)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        this.discardMyDisconnectedNodes();
    }

    private void removeEdge(String key) {
        this.idEdgeMap.remove(key);
    }

    private void removeNode(FPGrowthPlaceFollowGraphNode node) {
        int id = this.placeNodeIdMap.remove(node.getPlace());
        this.nodeIdNodeMap.remove(id);
    }

    @Override
    public Set<FPGrowthPlaceFollowGraphNode> getNodes() {
        return new HashSet<>(this.nodeIdNodeMap.values());
    }

    @Override
    public Set<FPGrowthPlaceFollowGraphEdge> getEdges() {
        return new HashSet<>(this.idEdgeMap.values());
    }

    public double getAverageNodeOutDegree() {
        int totalOutDegree = 0;
        for (FPGrowthPlaceFollowGraphNode source : this.getNodes()) {
            int outDegree = 0;
            for (FPGrowthPlaceFollowGraphNode target : this.getNodes()) {
                if (this.hasEdge(source, target)) {
                    outDegree++;
                }
            }
            totalOutDegree += outDegree;
        }
        return totalOutDegree * 1.0 / this.nodeIdNodeMap.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FPGrowthPlaceFollowGraph graph = (FPGrowthPlaceFollowGraph) o;
        return Objects.equals(placeNodeIdMap, graph.placeNodeIdMap) &&
                Objects.equals(nodeIdNodeMap, graph.nodeIdNodeMap) &&
                Objects.equals(idEdgeMap, graph.idEdgeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), placeNodeIdMap, nodeIdNodeMap, idEdgeMap);
    }

    public Set<FPGrowthPlaceFollowGraphNode> getNodesForDegree(int count, int degree) {
        List<Pair<FPGrowthPlaceFollowGraphNode, Integer>> degreeList = new ArrayList<>();
        for (FPGrowthPlaceFollowGraphNode source : this.getNodes()) {
            int outDegree = 0;
            for (FPGrowthPlaceFollowGraphNode target : this.getNodes()) {
                if (this.hasEdge(source, target)) {
                    outDegree++;
                }
            }
            degreeList.add(new Pair<>(source, outDegree));
        }
        degreeList.sort(Comparator.comparingInt(p -> Math.abs(p.getValue() - degree)));
        return degreeList.subList(0, count).stream().map(Pair::getKey).collect(Collectors.toSet());
    }
}
