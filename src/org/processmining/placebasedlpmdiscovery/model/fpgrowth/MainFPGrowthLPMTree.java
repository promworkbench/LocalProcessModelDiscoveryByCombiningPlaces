package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TraceSupportEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.interruptible.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

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

    private static void transferAdditionalInfo(MainFPGrowthLPMTreeNode node, LocalProcessModel lpm) {
        for (Map.Entry<String, LPMCollectorResult> entry : node.getAdditionalInfo().getCollectorResults().entrySet()) {
            lpm.getAdditionalInfo().addCollectorResult(entry.getKey(), entry.getValue());
        }
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
        Set<MainFPGrowthLPMTreeNode> visited = new HashSet<>();

        Queue<MainFPGrowthLPMTreeNode> queue = new LinkedList<>();
        queue.add(root);
        int counter = 0;
        int counter2 = 0;
        while (!queue.isEmpty()) {
            if (stop && lpms.size() >= count) {
                return lpms;
            }
            MainFPGrowthLPMTreeNode node = queue.poll();
            if (node != root && !node.getAdditionalInfo().getCollectorResults().isEmpty()) {
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

    public void updateAllTotalCount(WindowTotalCounter windowTotalCounter, Integer totalTraceCount, XLog log) {
        for (MainFPGrowthLPMTreeNode node : this.nodes) {
            for (LPMCollectorResult res : node.getAdditionalInfo().getCollectorResults().values()) {
                if (StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.equals(res.getId())) {
                    ((FittingWindowsEvaluationResult) res).setTotal(windowTotalCounter.getWindowCount());
                } else if (StandardLPMEvaluationResultId.TraceSupportEvaluationResult.equals(res.getId())) {
                    ((TraceSupportEvaluationResult) res).setTotalTraceCount(totalTraceCount);
                } else if (StandardLPMEvaluationResultId.EventCoverageEvaluationResult.equals(res.getId())) {
                    ((EventCoverageEvaluationResult) res).setEventCountPerActivity(LogUtils.getEventCountPerActivity(log));
                }
            }
        }
    }

    public int getHeight() {
        return root.getHeight();
    }

    @Override
    public void interrupt() {
        this.stop = true;
    }
}