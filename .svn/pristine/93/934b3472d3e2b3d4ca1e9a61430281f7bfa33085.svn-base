package org.processmining.placebasedlpmdiscovery.loganalyzer;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.evaluation.logs.WindowLog;

import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    private XLog log;
    private LogAnalyzerParameters parameters;
    private WindowLog windowLog;

    private LEFRMatrix lefrMatrix;

    public LogAnalyzer(XLog log, LogAnalyzerParameters parameters) {
        this.log = log;
        this.parameters = parameters;
        this.lefrMatrix = new LEFRMatrix(log, parameters.getDistanceLimit());
        windowLog = new WindowLog(log);
    }

    public void allVsDistinctWindowCount() {
        Map<List<Integer>, Integer> res = windowLog.getAllWindows(parameters.getDistanceLimit());
        int allWindows = res.values().stream().reduce(0, Integer::sum);
        int differentWindows = res.size();
        System.out.println(differentWindows + " / " + allWindows);
    }

    public int traceVariantCount() {
        return windowLog.getTraceVariantIds().size();
    }

    public int traceVariantTotalEvents() {
        int sum = 0;
        for (Integer id : windowLog.getTraceVariantIds()) {
            sum += windowLog.getTraceVariant(id).size();
        }
        return sum;
    }

    public int getActivityCount() {
        return windowLog.getLabelMap().size();
    }

    public void calculateLEFRMatrix() {
        this.lefrMatrix.calculateMatrix();
    }

    public LEFRMatrix getLEFRMatrix() {
        return lefrMatrix;
    }
}
