package org.processmining.placebasedlpmdiscovery.utils;

import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.out.XesXmlSerializer;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LogConverter {

    public static void main(String[] args) {
        try {
            XLog log = LogUtils.readLogFromFile("/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/10939.xes");
            XFactory factory = new XFactoryNaiveImpl();
            XAttributeMap attributeMap = new XAttributeMapImpl();
            attributeMap.put("concept:name", factory.createAttributeLiteral(
                    "concept:name", "bpi2012-10939", XExtensionManager.instance().getByPrefix("concept")
            ));

            XLog convertedLog = factory.createLog(attributeMap);

            Map<LocalDate, Set<XTrace>> dayToTracesMap = new HashMap<>();
            for (XTrace trace : log) {
                Set<LocalDate> days = trace
                        .stream()
                        .map(event -> new LocalDate(((XAttributeTimestampImpl) event.getAttributes().get(XTimeExtension.KEY_TIMESTAMP)).getValue()))
                        .collect(Collectors.toSet());
                if (days.size() > 1) {
                    System.out.println("More than one day in a trace");
                } else {
                    Set<XTrace> traces = dayToTracesMap.getOrDefault(days.iterator().next(), new HashSet<>());
                    traces.add(trace);
                    dayToTracesMap.put(days.iterator().next(), traces);
                }
            }
            for (LocalDate date : dayToTracesMap.keySet()) {
                convertedLog.add(createOneTrace(dayToTracesMap.get(date), factory));
            }
            XesXmlSerializer serializer = new XesXmlSerializer();
            File f = new File("/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/10939-days.xes");
            OutputStream os = new FileOutputStream(f);
            serializer.serialize(log, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static XTrace createOneTrace(Set<XTrace> xTraces, XFactory factory) {
        return null;
    }
}
