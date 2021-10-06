package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

public abstract class LPMResultGroup extends LPMResult {

    private static final long serialVersionUID = 5041306587143244956L;
    protected int commonId;


    public LPMResultGroup() {
        this.commonId = -1;
    }

    public abstract boolean shouldNotAdd(LocalProcessModel element);

    public abstract void initializeGroup(LocalProcessModel element);

    @Override
    public boolean add(LocalProcessModel element) {
        if (size() < 1) {
            this.initializeGroup(element);
        }

        if (this.shouldNotAdd(element))
            return false;

        return super.add(element);
    }
}
