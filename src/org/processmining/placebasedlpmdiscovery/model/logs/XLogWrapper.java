package org.processmining.placebasedlpmdiscovery.model.logs;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class XLogWrapper implements EventLog {

    private final XLog log;

    public XLogWrapper(XLog log) {
        this.log = log;
    }

    public static XLogWrapper fromListOfTracesAsListStrings(List<List<String>> traces) {
        XFactory factory = XFactoryRegistry.instance().currentDefault();
        XLog log = factory.createLog();

        for (List<String> trace : traces) {
            XTrace xTrace = factory.createTrace();
            for (String event : trace) {
                XEvent xEvent = factory.createEvent();
                XConceptExtension.instance().assignName(xEvent, event);
                xTrace.add(xEvent);
            }
            log.add(xTrace);
        }
        return new XLogWrapper(log);
    }

    @Override
    public Set<Activity> getActivities() {
        return LogUtils.getActivitiesFromLog(this.log).stream().map(l -> ActivityCache.getInstance().getActivity(l)).collect(Collectors.toSet());
    }

    @Override
    public XLog getOriginalLog() {
        return this.log;
    }
}
