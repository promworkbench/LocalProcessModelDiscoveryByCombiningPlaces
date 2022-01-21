package org.processmining.placebasedlpmdiscovery.utils;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilities that can be used over logs
 */
public class LogUtils {

    /**
     * Reads the event log that is written in the file with the given file path
     *
     * @param filePath: the file path of the file that contains the event log
     * @return object of type XLog that contains the event log from the file
     */
    public static XLog readLogFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        return readLogFromFile(file);
    }

    public static Collection<String> getActivitiesFromLog(XLog log) {
        XLogInfo info = XLogInfoFactory.createLogInfo(log);
        return info
//        getEventClasses(new XEventAndClassifier(new XEventNameClassifier(), new XEventLifeTransClassifier()))
                .getEventClasses(new XEventNameClassifier())
                .getClasses().stream()
                .map(XEventClass::getId)
                .collect(Collectors.toList());
    }

    /**
     * Reads the event log that is written in the file
     *
     * @param file: the file that contains the event log
     * @return object of type XLog that contains the event log from the file
     */
    public static XLog readLogFromFile(File file) throws Exception {
        XesXmlParser parser = new XesXmlParser();
        return parser.parse(file).get(0);
    }

    /**
     * Given the log take all pairs of events (their names in particular) that occur together in some specified
     * distance limit.
     *
     * @param log:                          the log from which we want to get all follow relations given some distance limit
     * @param followRelationsDistanceLimit: the distance limit in which the follow relation can happen
     * @return set of pairs, where each pair is consisted of the events that are in a follow relation
     */
    public static Set<Pair<String, String>> getFollowRelations(XLog log, int followRelationsDistanceLimit) {
        Set<Pair<String, String>> resSet = new HashSet<>();
        for (XTrace trace : log) {
            List<String> events = trace
                    .stream()
                    .map(event -> ((XAttributeLiteral) event.getAttributes().get(XConceptExtension.KEY_NAME)).getValue())
                    .collect(Collectors.toList());
            for (int i = 1; i < events.size(); ++i) {
                for (int j = Math.max(0, i + 1 - followRelationsDistanceLimit); j < i; ++j) {
                    resSet.add(new Pair<>(events.get(j), events.get(i)));
                }
            }
        }
        return resSet;
    }

    /**
     * Extract the event log name given the event log.
     * @param eventLog: the event log object
     * @return the name
     */
    public static String getEventLogName(XLog eventLog) {
        XAttribute nameAttribute = eventLog.getAttributes().get(XConceptExtension.KEY_NAME);
        if (nameAttribute != null) {
            return ((XAttributeLiteral) nameAttribute).getValue();
        }
        return null;
    }
}
