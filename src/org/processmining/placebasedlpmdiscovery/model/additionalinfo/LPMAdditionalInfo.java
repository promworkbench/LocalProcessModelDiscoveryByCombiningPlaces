package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.GroupedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LPMAdditionalInfo implements Serializable {

    public enum DefaultInfoKeys {

    }

    private static final long serialVersionUID = 3593199319792435898L;
    //    private double logFrequency;
//    private TraceVariantInfo[] traceVariantInfos;
    private transient LocalProcessModel lpm;
    private GroupedEvaluationResult evaluationResult;

    private Map<String, Object> infos;

    public LPMAdditionalInfo() {
        this.infos = new HashMap<>();
    }

    public LPMAdditionalInfo(LocalProcessModel lpm) {
        this.lpm = lpm;
        this.evaluationResult = new GroupedEvaluationResult(lpm);
        this.infos = new HashMap<>();
    }

    public LPMAdditionalInfo(LPMAdditionalInfo additionalInfo) {
        try {
            this.evaluationResult = additionalInfo.getEvaluationResult().clone();
            this.lpm = additionalInfo.lpm;
            this.infos = new HashMap<>(additionalInfo.infos);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

//    public TraceVariantInfo[] getTraceVariantInfos() {
//        return traceVariantInfos;
//    }
//
//    public double getLogFrequency() {
//        return logFrequency;
//    }
//
//    public void setLogFrequency(double logFrequency) {
//        this.logFrequency = logFrequency;
//    }

//    private void updateTraceVariantInfos(Place place) {
//        // TODO: Improve how we update missing tokens, frequency and fitness for the lpm depending on the place we add
//        if (this.traceVariantInfos == null)
//            this.traceVariantInfos = new TraceVariantInfo[place.getAdditionalInfo().getTraceVariantInfo().length];
//
//        for (int i = 0; i < this.traceVariantInfos.length; ++i) {
//            TraceVariantInfo placeInfo = place.getAdditionalInfo().getTraceVariantInfo()[i];
//            if (this.traceVariantInfos[i] == null) {
//                this.traceVariantInfos[i] = new TraceVariantInfo();
//                this.traceVariantInfos[i].setRemainingTokens(placeInfo.getRemainingTokens());
//                this.traceVariantInfos[i].setFrequency(placeInfo.getFrequency());
//                this.traceVariantInfos[i].setFit(placeInfo.isFit());
//            } else {
//                this.traceVariantInfos[i].setRemainingTokens(this.traceVariantInfos[i].getRemainingTokens()
//                        + placeInfo.getRemainingTokens());
//                this.traceVariantInfos[i].setFrequency(
//                        Math.min(this.traceVariantInfos[i].getFrequency(), placeInfo.getFrequency()));
//                this.traceVariantInfos[i].setFit(this.traceVariantInfos[i].isFit() && placeInfo.isFit());
//            }
//        }
//    }

    public GroupedEvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(GroupedEvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    public void clearEvaluation() {
        this.evaluationResult = new GroupedEvaluationResult(lpm);
    }

    public boolean existsInfo(String key) {
        return this.infos.containsKey(key);
    }

    public void addInfo(String key, AbstractEvaluationResult info) {
        this.infos.put(key, info);
    }

    public void updateInfo(String key, AbstractEvaluationResult info) {
        this.infos.put(key, evaluationResult);
    }

    public <T> T getInfo(String key, Class<T> infoClass) {
        Object info = this.infos.get(key);
        if (!infoClass.isInstance(info)) {
            throw new IllegalArgumentException("The info for the key " + key + " is of type " + info.getClass() +
                    " while the requested class is " + infoClass);
        }
        return infoClass.cast(info);
    }
}
