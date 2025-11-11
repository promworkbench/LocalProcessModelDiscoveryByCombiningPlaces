package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.processmining.lpms.model.LPM;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WindowLPMTreeWrapperAsWindowLPMStorage implements WindowLPMStorage {

    private final WindowLPMTree windowLPMTree;
    private final Set<Place> placeNets;

    public WindowLPMTreeWrapperAsWindowLPMStorage(WindowLPMTree windowLPMTree, Set<Place> placeNets) {
        this.windowLPMTree = windowLPMTree;
        this.placeNets = placeNets;
    }

    public Collection<LocalProcessModel> getAllValidLPMs() {
        Set<LocalProcessModel> lpms = new HashSet<>();
        Set<WindowLPMTreeNode> nullNodes = this.windowLPMTree.getNullNodes();
        for (WindowLPMTreeNode n : nullNodes) {
            LocalProcessModel lpm = LocalProcessModelUtils.convertReplayableToLPM(n.getLpm(), this.placeNets);
            lpms.add(lpm);
        }
        return lpms;
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return getAllValidLPMs();
    }

    @Override
    public boolean hasNext() {
        throw new NotImplementedException();
    }

    @Override
    public LPM next() {
        throw new NotImplementedException();
    }
}
