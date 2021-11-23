package org.processmining.placebasedlpmdiscovery.loganalyzer;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.analysis.LogStatistics;

import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    private final LogStatistics logStatistics;

    private final LogAnalyzerParameters parameters;
    private final WindowLog windowLog;

    private final LEFRMatrix lefrMatrix;

    public LogAnalyzer(XLog log, LogAnalyzerParameters parameters) {
        this(log, parameters, new LogStatistics());
    }

    public LogAnalyzer(XLog log, LogAnalyzerParameters parameters, LogStatistics logStatistics) {
        this.parameters = parameters;
        this.logStatistics = logStatistics;
        this.lefrMatrix = new LEFRMatrix(log, parameters.getDistanceLimit());
        this.windowLog = new WindowLog(log);
        this.logStatistics.setLogName(LogUtils.getEventLogName(log));
        this.logStatistics.setWindowSize(parameters.getDistanceLimit());
        this.analyze();
    }

    private void analyze() {
        Map<List<Integer>, Integer> res = windowLog.getAllWindows(parameters.getDistanceLimit());
        logStatistics.setAllWindowsCount(res.values().stream().reduce(0, Integer::sum));
        logStatistics.setDistinctWindowsCount(res.size());
        logStatistics.setTraceVariantsCount(windowLog.getTraceVariantIds().size());
        logStatistics.setTraceVariantsTotalEvents(this.traceVariantTotalEvents());
        logStatistics.setActivitiesCount(windowLog.getLabelMap().size());
    }

    private int traceVariantTotalEvents() {
        int sum = 0;
        for (Integer id : windowLog.getTraceVariantIds()) {
            sum += windowLog.getTraceVariant(id).size();
        }
        return sum;
    }

    public LEFRMatrix getLEFRMatrix() {
        return lefrMatrix;
    }

    public LogStatistics getLogStatistics() {
        return logStatistics;
    }
}
