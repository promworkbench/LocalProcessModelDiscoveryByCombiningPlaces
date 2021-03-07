package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableList;

public class GroupedLPMResult extends SerializableList<LPMResultGroup> {

    private static final long serialVersionUID = 6600828346790284313L;

    private GroupingProperty property;

    public GroupedLPMResult(LPMResult result, GroupingProperty property) {
        super();
        this.property = property;
        for (LocalProcessModel lpm : result.getElements()) {
            this.add(lpm);
        }
    }

    public boolean add(LocalProcessModel lpm) {
        boolean added;
        for (LPMResultGroup group : this.elements) {
            added = group.add(lpm);
            if (added)
                return true;
        }

        LPMResultGroup group = this.createGroup(this.property);
        group.add(lpm);
        add(group);
        return true;
    }

    private LPMResultGroup createGroup(GroupingProperty property) {
        if (property == GroupingProperty.SameActivities) {
            return new SameActivityLPMResultGroup();
        } else if (property == GroupingProperty.WindowCoverage) {
            return new WindowCoverageLPMResultGroup();
        }
        throw new IllegalArgumentException();
    }
}
