package org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTreeNode;

import java.util.*;

public class MainFPGrowthLPMTreeTraversal implements LPMBuildingResultTraversal {

    private final MainFPGrowthLPMTree result;

    private final Queue<MainFPGrowthLPMTreeNode> queue;

    public MainFPGrowthLPMTreeTraversal(MainFPGrowthLPMTree result) {
        this.result = result;
        this.queue = new LinkedList<>();
        this.queue.add(result.getRoot());
    }

    @Override
    public boolean hasNext() {
        while (!queue.isEmpty()) {
            MainFPGrowthLPMTreeNode node = queue.peek();
            queue.addAll(node.getChildren());
            if (node != result.getRoot() && !node.getAdditionalInfo().getCollectorResults().isEmpty()) {
                return true;
            }
            queue.poll();
        }
        return false;
    }

    @Override
    public LocalProcessModel next() {
        boolean stop = false;
        LocalProcessModel lpm = null;
        while (!stop) {
            MainFPGrowthLPMTreeNode node = queue.poll();
            if (node != result.getRoot()) {
                assert node != null;
                if (!node.getAdditionalInfo().getCollectorResults().isEmpty()) {
                    lpm = node.getLPM();
                    transferAdditionalInfo(node, lpm);
                    stop = true;
                }
            }
        }
        return lpm;
    }

    private static void transferAdditionalInfo(MainFPGrowthLPMTreeNode node, LocalProcessModel lpm) {
        for (Map.Entry<String, LPMCollectorResult> entry : node.getAdditionalInfo().getCollectorResults().entrySet()) {
            lpm.getAdditionalInfo().addCollectorResult(entry.getKey(), entry.getValue());
        }
    }
}
