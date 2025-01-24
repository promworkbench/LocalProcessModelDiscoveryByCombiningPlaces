package org.processmining.placebasedlpmdiscovery.grouping;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceService;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public interface GroupingService {

    static GroupingService getInstance(XLog log) {
        return new DefaultGroupingService(ModelDistanceService.getInstance(log));
    }

    void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config);
}
