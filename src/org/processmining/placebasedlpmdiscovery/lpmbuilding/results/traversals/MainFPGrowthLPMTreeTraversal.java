package org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTreeNode;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
        return queue.isEmpty();
    }

    @Override
    public LocalProcessModel next() {
        boolean stop = false;
        LocalProcessModel lpm = null;
        while (!stop) {
            MainFPGrowthLPMTreeNode node = queue.poll();
            if (node != result.getRoot() && !node.getAdditionalInfo().getCollectorResults().isEmpty()) {
                lpm = node.getLPM();
                transferAdditionalInfo(node, lpm);
                stop = true;
            }
            queue.addAll(node.getChildren());
        }
        return lpm;
    }

    private static void transferAdditionalInfo(MainFPGrowthLPMTreeNode node, LocalProcessModel lpm) {
        for (Map.Entry<String, LPMCollectorResult> entry : node.getAdditionalInfo().getCollectorResults().entrySet()) {
            lpm.getAdditionalInfo().addCollectorResult(entry.getKey(), entry.getValue());
        }
    }
}
