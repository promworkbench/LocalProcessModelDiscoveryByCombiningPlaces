package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class ClusteringLPMResultGroup extends LPMResultGroup {
    private final String groupingKey;

    public ClusteringLPMResultGroup(String clusteringKey) {
        this.groupingKey = clusteringKey;
    }

    @Override
    public boolean shouldNotAdd(LocalProcessModel element) {
        return !element.getAdditionalInfo().getGroupsInfo().getGroupingProperty(this.groupingKey).equals(this.commonId);
    }

    @Override
    public void initializeGroup(LocalProcessModel element) {
        this.commonId = element.getAdditionalInfo().getGroupsInfo().getGroupingProperty(this.groupingKey);
    }
}
