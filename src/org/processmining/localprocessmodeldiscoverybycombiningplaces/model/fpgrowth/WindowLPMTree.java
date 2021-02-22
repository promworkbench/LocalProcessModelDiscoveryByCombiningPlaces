package org.processmining.localprocessmodeldiscoverybycombiningplaces.model.fpgrowth;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.ReplayableLocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.CanBeInterrupted;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;

import java.util.*;

public class WindowLPMTree implements CanBeInterrupted {

    private final WindowLPMTreeNode root;

    private boolean stop;

    public WindowLPMTree(int windowSize) {
        this.root = new WindowLPMTreeNode(0, windowSize, new ReplayableLocalProcessModel());
    }

    public void add(int inEvent, int inPos, int outEvent, int outPos,
                    Set<Place> places, Set<List<Place>> pathsViaSilent,
                    Map<String, Integer> labelMap) {

        Set<WindowLPMTreeNode> nodes = getNodesThatCanFire(inEvent, inPos);
        nodes.add(root); // also add the root
        for (WindowLPMTreeNode node : nodes) { // to each such node
            if (stop)
                break;
            addPlaces(node, inEvent, inPos, outEvent, outPos, places, labelMap); // concat the places
            addPaths(node, inEvent, inPos, outEvent, outPos, pathsViaSilent, labelMap); // add the paths
        }
    }

    private Set<WindowLPMTreeNode> getNodesThatCanFire(int inEvent, int inPos) {
        Set<WindowLPMTreeNode> res = new HashSet<>();

        Queue<WindowLPMTreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            WindowLPMTreeNode node = queue.poll();
            res.addAll(node.getChildren(inEvent, inPos));
            queue.addAll(node.getChildren(inPos));
        }
        return res;
    }

    public Set<WindowLPMTreeNode> getNullNodes() {
        Set<WindowLPMTreeNode> res = new HashSet<>();

        Queue<WindowLPMTreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            WindowLPMTreeNode node = queue.poll();
            if (node.getNullChild() != null)
                res.add(node.getNullChild());
            else
                System.out.println(node.getLpm().toString());
            queue.addAll(node.getChildren());
        }
        return res;
    }

    private void addPlaces(WindowLPMTreeNode node, int inEvent, int inPos, int outEvent, int outPos,
                           Set<Place> places, Map<String, Integer> labelMap) {
        for (Place place : places) {
            if (stop)
                return;
            node.tryAddPlace(inEvent, inPos, outEvent, outPos, place, labelMap);
        }
    }

    private void addPaths(WindowLPMTreeNode node, int inEvent, int inPos, int outEvent, int outPos,
                          Set<List<Place>> paths, Map<String, Integer> labelMap) {
        for (List<Place> path : paths) {
            if (stop)
                return;
            node.tryAddPath(inEvent, inPos, outEvent, outPos, path, labelMap);
        }
    }

    public void refreshPosition(int pos) {
        root.children.set(pos, null, null);
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }
}