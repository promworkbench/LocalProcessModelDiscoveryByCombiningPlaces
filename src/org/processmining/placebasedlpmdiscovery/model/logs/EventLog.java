package org.processmining.placebasedlpmdiscovery.model.logs;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Set;

public interface EventLog {
    Set<Activity> getActivities();

    XLog getOriginalLog();
}
