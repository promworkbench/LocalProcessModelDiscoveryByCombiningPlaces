package org.processmining.placebasedlpmdiscovery.evaluation.logs;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.*;

public class IntegerMappedLog extends AbstractEvaluationLog {

    private Map<String, Integer> labelMap;
    private Map<Integer, String> reverseLabelMap;
    private int labelMapInd;
    private Set<Integer> invisible;

    // To every trace variant we have given an id
    private Map<Integer, List<Integer>> traceVariantIdsMap;
    // Map of trace variant and the number of times it appears in the log.
    // The trace variant is represented as list of integers, where the integers are taken from the label map.
    private Map<List<Integer>, Integer> traceVariants;

    public IntegerMappedLog(XLog log) {
        super(log);
    }

    public Map<String, Integer> getLabelMap() {
        return labelMap;
    }

    public Map<Integer, String> getReverseLabelMap() {
        return reverseLabelMap;
    }

//    public Map<List<Integer>, Integer> getTraceVariants() {
//        return traceVariants;
//    }

    public Set<Integer> getTraceVariantIds() {
        return traceVariantIdsMap.keySet();
    }

    public List<Integer> getTraceVariant(Integer traceVariantId) {
        return traceVariantIdsMap.get(traceVariantId);
    }

    public Integer getTraceVariantCount(List<Integer> traceVariant) {
        return traceVariants.get(traceVariant);
    }

    @Override
    protected void makeLog() {
        invisible = new HashSet<>();
        labelMap = new HashMap<>();
        reverseLabelMap = new HashMap<>();
        this.traceVariants = new HashMap<>();

        for (XTrace trace : this.originalLog) {
            List<Integer> eventList = new ArrayList<>();
            for (XEvent event : trace) {
                // check if the event has the concept:name attribute which is the label of the event
                if (!event.getAttributes().containsKey(XConceptExtension.KEY_NAME))
                    continue;

                // check if the event label is already in the map, if it is not add it
                XAttributeLiteral eventLabel = (XAttributeLiteral) event.getAttributes().get(XConceptExtension.KEY_NAME);
                if (!labelMap.containsKey(eventLabel.getValue())) {
                    labelMap.put(eventLabel.getValue(), this.labelMapInd);
                    reverseLabelMap.put(this.labelMapInd, eventLabel.getValue());
                    this.labelMapInd++;
                }

                eventList.add(labelMap.get(eventLabel.getValue()));
            }
            int count = this.traceVariants.getOrDefault(eventList, 0);
            this.traceVariants.put(eventList, count + 1);
        }

        this.traceVariantIdsMap = new HashMap<>();
        int traceVariantId = 0;
        for (List<Integer> traceVariant : this.traceVariants.keySet())
            this.traceVariantIdsMap.put(++traceVariantId, traceVariant);
    }

    public void addInvisibleTransitionsInLabelMap(Set<String> transitionLabels) {
        for (String label : transitionLabels) {
            if (this.labelMap.containsKey(label))
                continue;
            this.labelMap.put(label, this.labelMapInd);
            this.reverseLabelMap.put(this.labelMapInd, label);
            this.invisible.add(this.labelMapInd);
            this.labelMapInd++;
        }
    }

    public Set<Integer> getInvisible() {
        return this.invisible;
    }
}
