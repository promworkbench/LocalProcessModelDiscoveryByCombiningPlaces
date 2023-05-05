package org.processmining.placebasedlpmdiscovery.model.logs;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.HashSet;
import java.util.Set;

public class XLogWrapper implements EventLog {

    private final XLog log;

    public XLogWrapper(XLog log) {
        this.log = log;
    }

    @Override
    public Set<String> getActivities() {
        return new HashSet<>(LogUtils.getActivitiesFromLog(this.log));
    }
}
