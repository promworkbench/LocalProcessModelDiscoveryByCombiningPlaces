package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTreeNode;

import java.util.List;
import java.util.Set;

public class WindowLPMTreeWrapperAsWindowLPMStorage implements WindowLPMStorage {

    private final WindowLPMTree windowLPMTree;

    public WindowLPMTreeWrapperAsWindowLPMStorage(WindowLPMTree windowLPMTree) {
        this.windowLPMTree = windowLPMTree;
    }

    public List<LocalProcessModel> getAllValidLPMs() {
        Set<WindowLPMTreeNode> nullNodes = this.windowLPMTree.getNullNodes();
        for (WindowLPMTreeNode n : nullNodes) {
        }
        throw new NotImplementedException();
    }
}
