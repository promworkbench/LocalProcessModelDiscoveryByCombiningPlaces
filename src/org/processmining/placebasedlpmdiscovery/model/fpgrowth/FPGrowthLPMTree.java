package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import java.util.HashSet;
import java.util.Set;

public abstract class FPGrowthLPMTree<N> {

    protected Set<N> nodes;
    protected N root;

    public FPGrowthLPMTree() {
        nodes = new HashSet<>();
        root = createRoot();
        nodes.add(root);
    }

    protected abstract N createRoot();

    public Set<N> getNodes() {
        return nodes;
    }
}
