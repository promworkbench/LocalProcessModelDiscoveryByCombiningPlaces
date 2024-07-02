package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableList;

import java.util.Collection;

public class GroupedLPMResult extends SerializableList<LPMResultGroup> {

    private static final long serialVersionUID = 6600828346790284313L;

    private final GroupingProperty property;
    private final String key;

    public GroupedLPMResult(Collection<LocalProcessModel> lpms, GroupingProperty property) {
        this(lpms, property, "default");
    }

    public GroupedLPMResult(Collection<LocalProcessModel> lpms,GroupingProperty property, String groupingKey) {
        super();
        this.property = property;
        this.key = groupingKey;
        for (LocalProcessModel lpm : lpms) {
            this.add(lpm);
        }
    }

    public boolean add(LocalProcessModel lpm) {
        boolean added = false;
        for (LPMResultGroup group : this.elements) {
            added = added || group.add(lpm);
        }
        if (!added) {
            LPMResultGroup group = createGroup();
            group.add(lpm);
            add(group);
        }

        return true;
    }

    private LPMResultGroup createGroup() {
        if (property == GroupingProperty.SameActivities) {
            return new SameActivityLPMResultGroup();
        } else if (property == GroupingProperty.WindowCoverage) {
            return new WindowCoverageLPMResultGroup();
        } else if (property == GroupingProperty.Clustering) {
            return new ClusteringLPMResultGroup(key);
        }
        throw new IllegalArgumentException();
    }
}
