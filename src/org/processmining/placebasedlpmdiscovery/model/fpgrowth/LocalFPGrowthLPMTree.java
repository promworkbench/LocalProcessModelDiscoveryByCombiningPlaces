package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.model.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalFPGrowthLPMTree extends FPGrowthLPMTree<LocalFPGrowthLPMTreeNode>
        implements CanBeInterrupted {
    private Set<LocalFPGrowthLPMTreeNode> nullNodes;
    private final Map<Integer, Set<LocalFPGrowthLPMTreeNode>> nodesAddedInStepMap;
    private int windowStep;
    private int currentStep;
    private int removeStep;
    private boolean stop;

    public LocalFPGrowthLPMTree() {
        nullNodes = new HashSet<>();
        nodesAddedInStepMap = new HashMap<>();
        windowStep = 1;
        removeStep = 1;
    }

    @Override
    protected LocalFPGrowthLPMTreeNode createRoot() {
        return new LocalFPGrowthLPMTreeNode();
    }

    public Set<LocalFPGrowthLPMTreeNode> getNullNodes() {
        return nullNodes;
    }

    public void add(Integer event, Set<Place> places, Set<List<Place>> pathsViaSilent,
                    Map<String, Integer> labelMap, int eventPosition) {
        this.currentStep = windowStep + eventPosition;
        // find all nodes that can fire the event
        Set<LocalFPGrowthLPMTreeNode> nodes = getNodesThatCanFire(event, currentStep);
        nodes.add(root); // also add the root
        for (LocalFPGrowthLPMTreeNode node : nodes) { // to each such node
            if (stop)
                break;
            addPlaces(node, event, places, labelMap); // concat the places
            addPaths(node, event, pathsViaSilent, labelMap); // add the paths
            tryAddNullChild(node, event);
        }
    }

    public void removeOldestBranches() {
        Set<LocalFPGrowthLPMTreeNode> branchesForRemoval = this.nodesAddedInStepMap.remove(removeStep);
        if (branchesForRemoval == null) {
            this.removeStep++;
            return;
        }

        Set<LocalFPGrowthLPMTreeNode> nodesForRemoval = branchesForRemoval
                .stream()
                .flatMap(n -> Stream.concat(Stream.of(n), n.getAllDescendants().stream()))
                .collect(Collectors.toCollection(HashSet::new));
        this.nodes = new HashSet<>(Sets.difference(nodes, nodesForRemoval));

        Set<UUID> nullUUIDsForRemoval = branchesForRemoval
                .stream()
                .map(LocalFPGrowthLPMTreeNode::getNullAncestors)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<LocalFPGrowthLPMTreeNode> nullNodesForRemoval = nullNodes
                .stream()
                .filter(n -> nullUUIDsForRemoval.contains(n.getUuid()))
                .collect(Collectors.toSet());
//        this.nullNodes.removeAll(nullNodesForRemoval);
        this.nullNodes = new HashSet<>(Sets.difference(this.nullNodes, nullNodesForRemoval));

        this.removeStep++;
        this.windowStep++;
    }

    public void tryAddNullChildren(int event, int eventPosition) {
        this.currentStep = windowStep + eventPosition;
        Set<LocalFPGrowthLPMTreeNode> nodes = getNodesThatCanFire(event, currentStep);
        for (LocalFPGrowthLPMTreeNode node : nodes) {
            tryAddNullChild(node, event);
        }
    }

    private void tryAddNullChild(LocalFPGrowthLPMTreeNode node, Integer event) {
        // check if they become empty when they fire the event
        if (node.isEmptyWhenFire(event)) {
            LocalFPGrowthLPMTreeNode child = node.addNullChild(event);
            nodes.add(child);
            nullNodes.add(child);
        }
    }

    private Set<LocalFPGrowthLPMTreeNode> getNodesThatCanFire(Integer event, int eventPosition) {
        Set<LocalFPGrowthLPMTreeNode> res = new HashSet<>();
        Set<LocalFPGrowthLPMTreeNode> nodesAddedBeforeEventPosition = this.nodesAddedInStepMap
                .entrySet()
                .stream()
                .filter(e -> e.getKey() < eventPosition)
                .flatMap(e -> e.getValue().stream())
                .collect(Collectors.toSet());
        for (LocalFPGrowthLPMTreeNode node : this.nodes) {
            if (stop)
                return res;
            if (nodesAddedBeforeEventPosition.contains(node) && node.canLPMFire(event))
                res.add(node);
        }

        return res;
    }

    private void addPlaces(LocalFPGrowthLPMTreeNode node, Integer event, Set<Place> places, Map<String, Integer> labelMap) {
        for (Place place : places) {
            if (stop)
                return;
            add(node, event, place, labelMap);
        }
    }

    private void addPaths(LocalFPGrowthLPMTreeNode node, Integer event, Set<List<Place>> paths, Map<String, Integer> labelMap) {
        for (List<Place> path : paths) {
            if (stop)
                return;
            LocalFPGrowthLPMTreeNode lastNode = add(node, event, path.get(0), labelMap);
            Place previous = path.get(0);
            for (int i = 1; i < path.size(); ++i) {
                Place next = path.get(i);
                Transition silent = PlaceUtils.getCommonSilentTransition(previous, next);
                if (lastNode == null || silent == null) // TODO: This all operation might need to be added in batch
                    // TODO: because we either want to have the whole path or nothing
                    break;
                lastNode = add(lastNode, labelMap.get(silent.getLabel()), next, labelMap);
            }
        }
    }

    private LocalFPGrowthLPMTreeNode add(LocalFPGrowthLPMTreeNode node, Integer event, Place place,
                                         Map<String, Integer> labelMap) {
        if (!node.canAddChild(event, place, labelMap))
            return null;

        LocalFPGrowthLPMTreeNode child = node.addChild(event, place, labelMap);
        nodes.add(child);
        Set<LocalFPGrowthLPMTreeNode> stepNodes = nodesAddedInStepMap.getOrDefault(currentStep, new HashSet<>());
        stepNodes.add(child);
        nodesAddedInStepMap.put(currentStep, stepNodes);
        return child;
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }
}

