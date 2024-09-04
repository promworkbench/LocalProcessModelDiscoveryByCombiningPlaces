package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.placecombination.fpgrowth.utils;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.Set;

public class WindowLPMTreeValidLPMsRandomTraversal {

    private final Set<WindowLPMTreeNode> validLpms;

    public WindowLPMTreeValidLPMsRandomTraversal(WindowLPMTree tree) {
        this.validLpms = tree.getNullNodes();
    }

    public boolean hasNext() {
        return !this.validLpms.isEmpty();
    }

    public WindowLPMTreeNode next() {
       return validLpms.stream().findAny().get();
    }
}
