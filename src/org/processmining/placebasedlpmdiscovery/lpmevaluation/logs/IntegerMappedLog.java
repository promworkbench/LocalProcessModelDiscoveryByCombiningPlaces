package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.*;

public class IntegerMappedLog extends AbstractEnhancedLog {

    public class Mapping {
        private final Map<String, Integer> labelMap;
        private final Map<Integer, String> reverseLabelMap;
        private int labelMapInd;
        private final Set<Integer> invisible;

        public Mapping() {
            invisible = new HashSet<>();
            labelMap = new HashMap<>();
            reverseLabelMap = new HashMap<>();
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

        public Map<String, Integer> getLabelMap() {
            return labelMap;
        }

        public Map<Integer, String> getReverseLabelMap() {
            return reverseLabelMap;
        }

        public Map<List<Integer>, Integer> getTraceVariants() {
            return traceVariants;
        }

        public Set<Integer> getInvisible() {
            return this.invisible;
        }

        public Integer getMapping(String label) {
            // if the label is not in the map create the mapping and return it
            if (!this.labelMap.containsKey(label)) {
                labelMap.put(label, this.labelMapInd);
                reverseLabelMap.put(this.labelMapInd, label);
                this.labelMapInd++;
                return this.labelMapInd - 1;
            }
            return this.labelMap.get(label);
        }
    }

    // The mapping from the original activities in the event log to the integer labels
    private Mapping mapping;

    // To every trace variant we have given an id
    private Map<Integer, List<Integer>> traceVariantIdsMap;

    // Map of trace variant and the number of times it appears in the log.
    // The trace variant is represented as list of integers, where the integers are taken from the label map.
    private Map<List<Integer>, Integer> traceVariants;

    public IntegerMappedLog(XLog log) {
        super(log);
    }

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
        this.mapping = new Mapping();
        this.traceVariants = new HashMap<>();

        for (XTrace trace : this.originalLog) {
            List<Integer> eventList = new ArrayList<>();
            for (XEvent event : trace) {
                // check if the event has the concept:name attribute which is the label of the event
                if (!event.getAttributes().containsKey(XConceptExtension.KEY_NAME))
                    continue;

                // get mapping for label
                XAttributeLiteral eventLabel = (XAttributeLiteral) event.getAttributes().get(XConceptExtension.KEY_NAME);
                eventList.add(this.mapping.getMapping(eventLabel.getValue()));
            }
            int count = this.traceVariants.getOrDefault(eventList, 0);
            this.traceVariants.put(eventList, count + 1);
        }

        this.traceVariantIdsMap = new HashMap<>();
        int traceVariantId = 0;
        for (List<Integer> traceVariant : this.traceVariants.keySet())
            this.traceVariantIdsMap.put(++traceVariantId, traceVariant);
    }

    public Mapping getMapping() {
        return mapping;
    }
}
