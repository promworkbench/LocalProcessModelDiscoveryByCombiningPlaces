package org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer;

import org.deckfour.xes.model.XLog;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.logs.WindowLog;

import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    private XLog log;
    private LogAnalyzerParameters parameters;

    private LEFRMatrix lefrMatrix;

    public LogAnalyzer(XLog log, LogAnalyzerParameters parameters) {
        this.log = log;
        this.parameters = parameters;
        this.lefrMatrix = new LEFRMatrix(log, parameters.getDistanceLimit());
    }

    public void allVsDistinctWindowCount() {
        WindowLog windowLog = new WindowLog(log);
        Map<List<Integer>, Integer> res = windowLog.getAllWindows(parameters.getDistanceLimit());
        int allWindows = res.values().stream().reduce(0, Integer::sum);
        int differentWindows = res.size();
        System.out.println(differentWindows + " / " + allWindows);
    }

    public void calculateLEFRMatrix() {
        this.lefrMatrix.calculateMatrix();
    }

    public LEFRMatrix getLEFRMatrix() {
        return lefrMatrix;
    }
}
