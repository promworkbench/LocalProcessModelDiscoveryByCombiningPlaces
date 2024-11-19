package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

public interface RemappedActivitiesLog<T> extends EventLog {

    AbstractActivityMapping<T> getMapping();
}
