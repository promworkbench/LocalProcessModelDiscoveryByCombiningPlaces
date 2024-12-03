package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.interruptible.CanBeInterrupted;

import java.util.*;
import java.util.function.Function;

public class WindowLPMTree implements CanBeInterrupted {

    private final WindowLPMTreeNode root;

    private boolean stop;

    public WindowLPMTree(int windowSize) {
        this.root = new WindowLPMTreeNode(
                0,
                windowSize,
                new ReplayableLocalProcessModel(),
                -1,
                -1,
                new ArrayList<>());
    }

    public void add(int inEvent, int inPos, int outEvent, int outPos,
                    Set<Place> places, Set<List<Place>> pathsViaSilent,
                    Function<String, Integer> labelMapper) {

        Set<WindowLPMTreeNode> nodes = getNodesThatCanFire(inEvent, inPos);
        nodes.add(root); // also add the root
        for (WindowLPMTreeNode node : nodes) { // to each such node
            if (stop)
                break;
            addPlaces(node, inEvent, inPos, outEvent, outPos, places, labelMapper); // concat the places
            addPaths(node, inEvent, inPos, outEvent, outPos, pathsViaSilent, labelMapper); // add the paths
        }
    }

    public void tryAddNullChildren(int event, int eventPos) {
        Set<WindowLPMTreeNode> nodes = getNodesThatCanFire(event, eventPos);
        for (WindowLPMTreeNode node : nodes) {
            if (stop)
                return;
            if (node.getNullChild() == null)
                node.tryAddNullChild(event, eventPos);
        }
    }

    private Set<WindowLPMTreeNode> getNodesThatCanFire(int inEvent, int inPos) {
        Set<WindowLPMTreeNode> res = new HashSet<>();

        Queue<WindowLPMTreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            if (stop)
                return res;
            WindowLPMTreeNode node = queue.poll();
            res.addAll(node.getChildren(inEvent, inPos));
            queue.addAll(node.getChildren(inPos));
        }
        return res;
    }

    public Set<WindowLPMTreeNode> getNullNodes() {
        Set<WindowLPMTreeNode> res = new HashSet<>();

        Queue<WindowLPMTreeNode> queue = new LinkedList<>(root.getChildren());
        while (!queue.isEmpty()) {
            if (stop) {
                return res;
            }
            WindowLPMTreeNode node = queue.poll();
            if (node.getNullChild() != null)
                res.add(node.getNullChild());
            queue.addAll(node.getChildren());
        }
        return res;
    }

    private void addPlaces(WindowLPMTreeNode node, int inEvent, int inPos, int outEvent, int outPos,
                           Set<Place> places, Function<String, Integer> labelMapper) {
        for (Place place : places) {
            if (stop)
                return;
            node.tryAddPlace(inEvent, inPos, outEvent, outPos, place, labelMapper);
        }
    }

    private void addPaths(WindowLPMTreeNode node, int inEvent, int inPos, int outEvent, int outPos,
                          Set<List<Place>> paths, Function<String, Integer> labelMapper) {
        for (List<Place> path : paths) {
            if (stop)
                return;
            node.tryAddPath(inEvent, inPos, outEvent, outPos, path, labelMapper);
        }
    }

    public void refreshPosition(int pos) {
        root.children.set(pos, null, null);
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }

    @Override
    public String toString() {
        return "WindowLPMTree{" +
                "root=" + root +
                '}';
    }
}