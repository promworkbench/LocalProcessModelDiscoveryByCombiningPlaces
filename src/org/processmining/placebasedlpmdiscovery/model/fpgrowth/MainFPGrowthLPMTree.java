package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.evaluation.results.concrete.WindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.evaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.*;

public class MainFPGrowthLPMTree extends FPGrowthLPMTree<MainFPGrowthLPMTreeNode> implements CanBeInterrupted {

    private final Map<Place, Integer> priorityMap; // place mapped into priority value
    private final Map<String, Integer> labelMap; // label mapped into integer id
    private final int maxDependencyLength;
    private boolean stop;

    public MainFPGrowthLPMTree(Map<Place, Integer> priorityMap, Map<String, Integer> labelMap, int maxDependencyLength) {
        this.priorityMap = priorityMap;
        this.labelMap = labelMap;
        this.maxDependencyLength = maxDependencyLength;
    }

    @Override
    protected MainFPGrowthLPMTreeNode createRoot() {
        return new MainFPGrowthLPMTreeNode(null);
    }

    public void addOrUpdate(LocalProcessModel lpm, int count, List<Integer> window, List<Integer> firingSequence) {
        List<Place> places = sortPlaces(lpm.getPlaces());

        if (places.size() < 1)
            System.out.println("STOP");

        MainFPGrowthLPMTreeNode current = root;
        for (int i = places.size() - 1; i >= 0; --i) {
            if (current.hasChild(places.get(i))) {
                current = current.getChild(places.get(i));
            } else {
                current = current.add(current, places.get(i));
                this.nodes.add(current);
            }
        }
        if (current.getWindowsEvaluationResult() == null)
            current.setWindowsEvaluationResult(new WindowsEvaluationResult(lpm, this.maxDependencyLength, this.labelMap));
        current.updateEvaluation(count, window, firingSequence);
    }

    private List<Place> sortPlaces(Set<Place> places) {
        List<Place> sorted = new ArrayList<>(places);
        sorted.sort(Comparator.comparingInt(priorityMap::get));
        return sorted;
    }

    public Set<LocalProcessModel> getLPMs(LPMFiltrationAndEvaluationController filtrationController, int count) {
        Set<LocalProcessModel> lpms = new HashSet<>();

        Queue<MainFPGrowthLPMTreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            if (stop && lpms.size() >= count) {
                return lpms;
            }
            MainFPGrowthLPMTreeNode node = queue.poll();
            if (node != root && node.getWindowsEvaluationResult() != null) {
                LocalProcessModel lpm = node.getLPM();
                lpm.getAdditionalInfo().getEvaluationResult().addResult(node.getWindowsEvaluationResult());
                if (filtrationController.shouldKeepLPM(lpm))
                    lpms.add(lpm);
            }
            queue.addAll(node.getChildren());
        }

        // TODO: The count of lpms we want returned should be used here since we don't want to go through all lpms.
        // TODO: How not sure yet

        return lpms;
    }

    public void updateAllTotalCount(WindowTotalCounter windowTotalCounter) {
        for (MainFPGrowthLPMTreeNode node : this.nodes)
            if (node.getWindowsEvaluationResult() != null)
                node.getWindowsEvaluationResult().setTotal(windowTotalCounter);
    }

    public int getHeight() {
        return root.getHeight();
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }
}