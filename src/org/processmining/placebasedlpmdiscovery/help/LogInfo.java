package org.processmining.placebasedlpmdiscovery.help;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.XTimeBounds;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class LogInfo {

    private Map<String, Set<String>> eventAttributeStringKeyValueMap;
    private Map<String, Pair<Date, Date>> eventAttributeDateKeyRangeMap;


    public LogInfo(XLog log) {
        init(log);
    }

    private void init(XLog log) {
        XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
        XAttributeInfo traceAttributeInfo = logInfo.getTraceAttributeInfo(); // TODO: use also trace attributes

        XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
        Collection<XAttribute> eventAttributes = eventAttributeInfo.getAttributes();
        for (XAttribute attr : eventAttributes) {
            attr.getAttributes();
        }

        System.out.println(eventAttributeInfo.getAttributeKeys());
        XTimeBounds logTimeBounds = logInfo.getLogTimeBoundaries();
        System.out.println(logTimeBounds.toString());


        List<Long> traceDurations = log
                .stream()
                .map(logInfo::getTraceTimeBoundaries).map(tb -> Duration
                        .between(tb.getStartDate().toInstant(), tb.getEndDate().toInstant()).toDays())
                .collect(Collectors.toList());
        System.out.println(traceDurations);
        System.out.println("Min trace duration: " + (traceDurations.stream().min(Long::compare).get()));
        System.out.println("Max trace duration: " + traceDurations.stream().max(Long::compare).get());
        System.out.println("Avg trace duration: " + traceDurations.stream().mapToInt(Long::intValue).sum() * 1.0 / traceDurations.size());
    }
}
