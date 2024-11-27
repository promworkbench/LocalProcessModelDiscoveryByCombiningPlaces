package org.processmining.placebasedlpmdiscovery.model.lpmstorage.traversals;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

public class LPMStorageTraversalFactory {

    public static GlobalLPMStorageTraversal createTraversal(GlobalLPMStorage lpmStorage) {
        if (lpmStorage instanceof MainFPGrowthLPMTree) {
            return new MainFPGrowthLPMTreeTraversal((MainFPGrowthLPMTree) lpmStorage);
        }
        throw new NotImplementedException("No traversal is implemented for a GlobalLPMStorage of type: " + lpmStorage.getClass());
    }
}
