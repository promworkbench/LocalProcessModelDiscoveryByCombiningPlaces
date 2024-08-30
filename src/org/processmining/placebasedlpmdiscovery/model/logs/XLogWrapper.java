package org.processmining.placebasedlpmdiscovery.model.logs;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class XLogWrapper implements EventLog {

    private final XLog log;

    public XLogWrapper(XLog log) {
        this.log = log;
    }

    @Override
    public Set<Activity> getActivities() {
        return LogUtils.getActivitiesFromLog(this.log).stream().map(SimpleActivity::new).collect(Collectors.toSet());
    }

    @Override
    public XLog getOriginalLog() {
        return this.log;
    }
}
