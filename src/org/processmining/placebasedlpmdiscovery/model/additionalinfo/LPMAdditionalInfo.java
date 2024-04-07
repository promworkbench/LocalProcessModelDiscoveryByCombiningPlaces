package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.*;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LPMAdditionalInfo implements Serializable {
    private static final long serialVersionUID = 3593199319792435898L;

    private transient LocalProcessModel lpm;
    private final Map<String, LPMCollectorResult> collectorResults;
    private GroupsInfo groupsInfo;

    public LPMAdditionalInfo() {
        this.collectorResults = new HashMap<>();
    }

    public LPMAdditionalInfo(LocalProcessModel lpm) {
        this.lpm = lpm;
        this.collectorResults = new HashMap<>();
        this.groupsInfo = new GroupsInfo();
    }

    public LPMAdditionalInfo(LPMAdditionalInfo additionalInfo) {
        this.lpm = additionalInfo.lpm;
        this.collectorResults = new HashMap<>(additionalInfo.collectorResults);
        this.groupsInfo = additionalInfo.getGroupsInfo();
    }

//    public GroupedEvaluationResult getEvaluationResult() {
//        return evaluationResult;
//    }

//    public void clearEvaluation() {
//        this.evaluationResult = new GroupedEvaluationResult(lpm);
//    }

    public boolean existsCollectorResult(String key) {
        return this.collectorResults.containsKey(key);
    }

    public void addCollectorResult(String key, LPMCollectorResult collectorResult) {
        this.collectorResults.put(key, collectorResult);
    }

    public void updateCollectorResults(String key, LPMCollectorResult collectorResult) {
        this.collectorResults.put(key, collectorResult);
    }

    public Map<String, LPMCollectorResult> getCollectorResults() {
        return collectorResults;
    }

    public Map<String, LPMEvaluationResult> getEvaluationResults() {
        return collectorResults.entrySet().stream()
                .filter(e -> e.getValue() instanceof LPMEvaluationResult)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (LPMEvaluationResult) e.getValue()));
    }

    public <T> T getEvaluationResult(String key, Class<T> infoClass) {
        Object info = this.collectorResults.get(key);
        if (info == null) {
            return null;
        }
        if (!infoClass.isInstance(info)) {
            throw new IllegalArgumentException("The info for the key " + key + " is of type " + info.getClass() +
                    " while the requested class is " + infoClass);
        }
        return infoClass.cast(info);
    }

    public GroupsInfo getGroupsInfo() {
        return groupsInfo;
    }

    public void setGroupsInfo(GroupsInfo groupsInfo) {
        this.groupsInfo = groupsInfo;
    }

}
