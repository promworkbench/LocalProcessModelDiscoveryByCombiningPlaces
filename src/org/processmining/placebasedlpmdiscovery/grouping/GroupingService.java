package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public interface GroupingService {

    void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config);
}
