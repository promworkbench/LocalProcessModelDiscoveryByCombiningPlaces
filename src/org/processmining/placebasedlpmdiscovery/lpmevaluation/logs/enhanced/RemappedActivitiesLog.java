package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;

public interface RemappedActivitiesLog<T> {

    AbstractActivityMapping<T> getMapping();
}
