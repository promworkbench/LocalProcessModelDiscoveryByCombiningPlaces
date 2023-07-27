package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.WindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.interruptible.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.*;

public class MainFPGrowthLPMTree extends FPGrowthLPMTree<MainFPGrowthLPMTreeNode> implements CanBeInterrupted {

    private final Map<Place, Integer> priorityMap; // place mapped into priority value
    private final Map<String, Integer> labelMap; // label mapped into integer id
    private final int maxDependencyLength;
    private boolean stop;
    private RunningContext runningContext;

    public MainFPGrowthLPMTree(Map<Place, Integer> priorityMap,
                               Map<String, Integer> labelMap,
                               int maxDependencyLength,
                               RunningContext runningContext) {
        this.priorityMap = priorityMap;
        this.labelMap = labelMap;
        this.maxDependencyLength = maxDependencyLength;
        this.runningContext = runningContext;
    }

    @Override
    protected MainFPGrowthLPMTreeNode createRoot() {
        return new MainFPGrowthLPMTreeNode(null);
    }

    public void addOrUpdate(LocalProcessModel lpm, int count, List<Integer> window, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, Integer traceVariantId) {
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
        this.updateAdditionalInfos(lpm, lpmTemporaryWindowInfo, current);
        if (current.getWindowsEvaluationResult() == null)
            current.setWindowsEvaluationResult(new WindowsEvaluationResult(lpm, this.maxDependencyLength, this.labelMap));
        current.updateEvaluation(count, window, lpmTemporaryWindowInfo, traceVariantId);
    }

    private void updateAdditionalInfos(LocalProcessModel lpm,
                                       LPMTemporaryWindowInfo tempInfo,
                                       MainFPGrowthLPMTreeNode treeNode) {
        this.runningContext
                .getLpmEvaluationController()
                .evaluateForOneWindow(lpm, tempInfo, treeNode.getAdditionalInfo());
    }

    private List<Place> sortPlaces(Set<Place> places) {
        List<Place> sorted = new ArrayList<>(places);
        sorted.sort(Comparator.comparingInt(priorityMap::get));
        return sorted;
    }

    public Set<LocalProcessModel> getLPMs(int count) {
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
                transferAdditionalInfo(node, lpm);
                if (this.runningContext.getLpmFiltrationController().shouldKeepLPM(lpm))
                    lpms.add(lpm);
            }
            queue.addAll(node.getChildren());
        }

        // TODO: The count of lpms we want returned should be used here since we don't want to go through all lpms.
        // TODO: How not sure yet

        return lpms;
    }

    private static void transferAdditionalInfo(MainFPGrowthLPMTreeNode node, LocalProcessModel lpm) {
        lpm.getAdditionalInfo().getEvaluationResult().addResult(node.getWindowsEvaluationResult());
        for (Map.Entry<String, LPMEvaluationResult> entry : node.getAdditionalInfo().getEvalResults().entrySet()) {
            lpm.getAdditionalInfo().addEvaluationResult(entry.getKey(), entry.getValue());
        }
    }

    public void updateAllTotalCount(WindowTotalCounter windowTotalCounter, Integer totalTraceCount) {
        for (MainFPGrowthLPMTreeNode node : this.nodes)
            if (node.getWindowsEvaluationResult() != null)
                node.getWindowsEvaluationResult().setTotal(windowTotalCounter, totalTraceCount);
    }

    public int getHeight() {
        return root.getHeight();
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }
}