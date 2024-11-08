package org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

public class LPMBuildingResultTraversalFactory {

    public static LPMBuildingResultTraversal createTraversal(LPMBuildingResult result) {
        if (result instanceof MainFPGrowthLPMTree) {
            return new MainFPGrowthLPMTreeTraversal((MainFPGrowthLPMTree) result);
        }
        throw new NotImplementedException("No traversal is implemented for a LPMBuildingResult of type: " + result.getClass());
    }
}
