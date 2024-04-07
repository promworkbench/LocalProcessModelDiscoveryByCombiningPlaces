package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;

import java.util.*;

public class ContextWindowLog {

    private final IntegerMappedLog integerMappedActivityLog;
    private final IntegerMappedLog integerMappedTraceVariantLog;
    private final Map<String, AttributeSummary<?,?>> context;

    public ContextWindowLog(XLog log, Map<String, AttributeSummary<?,?>> attributeSummary) {
        this.context = attributeSummary;
        XLog contextLog = createContextLog(log);
        this.integerMappedTraceVariantLog = new IntegerMappedLog(contextLog);
        this.integerMappedActivityLog = new IntegerMappedLog(log);
    }

    private XLog createContextLog(XLog log) {
        XFactory factory = new XFactoryNaiveImpl();
        XLog contextLog = factory.createLog();
        for (XTrace trace : log) {
            XTrace contextTrace = factory.createTrace(trace.getAttributes());
            for (XEvent event :trace) {
                XAttributeMap newAttributes = (XAttributeMap) event.getAttributes().clone();
                String conceptName = ((XAttributeLiteral) newAttributes.get(XConceptExtension.KEY_NAME)).getValue();
                XAttributeLiteral attribute = ((XAttributeLiteral) newAttributes.get(XConceptExtension.KEY_NAME));
                attribute.setValue(conceptName + "+" + checkInContext(event));
                XEvent newEvent = factory.createEvent(newAttributes);
                contextTrace.add(newEvent);
            }
            contextLog.add(contextTrace);
        }
        return contextLog;
    }

    private boolean checkInContext(XEvent event) {
        for (XAttribute attribute : event.getAttributes().values()) {
            if (context.containsKey(attribute.getKey())
                    && !context.get(attribute.getKey()).acceptValue(attribute))
                return false;
        }
        return true;
    }

    public Map<List<Integer>, Integer> getWindowsForTraceVariant(Integer traceVariantId, int windowSize) {
        // TODO: Check if pagination can be used
        Map<List<Integer>, Integer> windows = new HashMap<>();
        LinkedList<Integer> currentWindow = new LinkedList<>();
        for (int event : this.integerMappedTraceVariantLog.getTraceVariant(traceVariantId)) {
            if (currentWindow.size() >= windowSize)
                currentWindow.removeFirst();
            currentWindow.add(event);
            int count = windows.getOrDefault(new ArrayList<>(currentWindow), 0);
            windows.put(new ArrayList<>(currentWindow), count + 1);
        }
        while (currentWindow.size() > 1) {
            currentWindow.removeFirst();
            int count = windows.getOrDefault(new ArrayList<>(currentWindow), 0);
            windows.put(new ArrayList<>(currentWindow), count + 1);
        }

        return windows;
    }

    public AbstractActivityMapping<Integer> getMapping() {
        return this.integerMappedActivityLog.getMapping();
    }

    public Set<Integer> getTraceVariantIds() {
        return this.integerMappedTraceVariantLog.getTraceVariantIds();
    }

    public List<Integer> getTraceVariant(Integer traceVariantMappedTraceVariantId) {
        return remapToActivityTrace(this.integerMappedTraceVariantLog.getTraceVariant(traceVariantMappedTraceVariantId));
    }

    private List<Integer> remapToActivityTrace(List<Integer> traceVariantTrace) {
        List<Integer> activityTrace = new ArrayList<>();
        traceVariantTrace.forEach(e -> {
            String traceVariantOriginal = this.integerMappedTraceVariantLog.getMapping().getReverseLabelMap().get(e);
            Integer activityMapping = this.integerMappedActivityLog.getMapping()
                    .getMapping(traceVariantOriginal.substring(0, traceVariantOriginal.lastIndexOf('+')));
            activityTrace.add(activityMapping);
        });
        return activityTrace;
    }

    public int getTraceVariantCount(List<Integer> traceVariantTraceVariant) {
        return this.integerMappedTraceVariantLog.getTraceVariantCount(traceVariantTraceVariant);
    }

    public List<Integer> getTraceVariantContext(Integer traceVariantId) {
        return this.integerMappedTraceVariantLog.getTraceVariant(traceVariantId);
    }

    public boolean isUsable(Integer traceVariantId, int eventPos) {
        String label = this.integerMappedTraceVariantLog.getMapping().getReverseLabelMap().get(
                this.integerMappedTraceVariantLog.getTraceVariant(traceVariantId).get(eventPos));

        return "true".equals(label.substring(label.lastIndexOf('+') + 1));
    }

    public Integer getTraceCount() {
        return this.integerMappedTraceVariantLog.getTraceCount();
    }
}
