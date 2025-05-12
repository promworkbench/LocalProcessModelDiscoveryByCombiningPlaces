package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.lang.NotImplementedException;
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
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.interruptible.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.traversals.GlobalLPMStorageTraversal;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.traversals.LPMStorageTraversalFactory;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.*;

public class MainFPGrowthLPMTree extends FPGrowthLPMTree<MainFPGrowthLPMTreeNode> implements CanBeInterrupted,
        GlobalLPMStorage {

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

    public MainFPGrowthLPMTree(Map<Place, Integer> priorityMap,
                               Map<String, Integer> labelMap,
                               int maxDependencyLength) {
        this.priorityMap = priorityMap;
        this.labelMap = labelMap;
        this.maxDependencyLength = maxDependencyLength;
    }

    @Override
    protected MainFPGrowthLPMTreeNode createRoot() {
        return new MainFPGrowthLPMTreeNode(null);
    }

    public MainFPGrowthLPMTreeNode getNode(LocalProcessModel lpm) {
        List<Place> places = sortPlaces(lpm.getPlaces());

        MainFPGrowthLPMTreeNode current = root;
        for (int i = places.size() - 1; i >= 0; --i) {
            if (current.hasChild(places.get(i))) {
                current = current.getChild(places.get(i));
            } else {
                return null;
            }
        }
        return current;
    }

    public void addOrUpdate(LocalProcessModel lpm, LPMAdditionalInfo additionalInfo) {
        List<Place> places = sortPlaces(lpm.getPlaces());

        MainFPGrowthLPMTreeNode current = root;
        for (int i = places.size() - 1; i >= 0; --i) {
            if (current.hasChild(places.get(i))) {
                current = current.getChild(places.get(i));
            } else {
                current = current.add(current, places.get(i));
                this.nodes.add(current);
            }
        }
        current.setAdditionalInfo(additionalInfo);
    }

    public void addOrUpdate(LocalProcessModel lpm, int count, List<Integer> window,
                            LPMTemporaryWindowInfo lpmTemporaryWindowInfo, Integer traceVariantId) {
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
                .updateAdditionalInfoForOneWindow(lpm, tempInfo, treeNode.getAdditionalInfo());
    }

    private List<Place> sortPlaces(Set<Place> places) {
        List<Place> sorted = new ArrayList<>(places);
        sorted.sort(Comparator.comparingInt(priorityMap::get));
        return sorted;
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

    public MainFPGrowthLPMTreeNode getRoot() {
        return this.root;
    }

    @Override
    public boolean add(LocalProcessModel lpm) {
        throw new NotImplementedException("This class should not be a global storage");
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        Collection<LocalProcessModel> lpms = new HashSet<>();
        GlobalLPMStorageTraversal traversal = LPMStorageTraversalFactory.createTraversal(this);
        while (traversal.hasNext()) {
            lpms.add(traversal.next());
        }
        return lpms;
    }
}