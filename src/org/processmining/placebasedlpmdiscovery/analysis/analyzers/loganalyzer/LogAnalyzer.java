package org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.analysis.statistics.LogStatistics;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

public class LogAnalyzer {

    private final LogStatistics logStatistics;

    private final XLog eventLog;
//    private final WindowLog windowLog;

    private LEFRMatrix lefrMatrix;

    public LogAnalyzer(XLog log) {
        this(log, new LogStatistics());
    }

    public LogAnalyzer(XLog log, LogStatistics logStatistics) {
        this.logStatistics = logStatistics;
        this.eventLog = log;
//        this.windowLog = new WindowLog(log);
        this.logStatistics.setLogName(LogUtils.getEventLogName(log));
    }

    public void analyze(int windowSize) {
//        Map<List<Integer>, Integer> res = windowLog.getAllWindows(windowSize);
        this.logStatistics.setWindowSize(windowSize);
//        logStatistics.setAllWindowsCount(res.values().stream().reduce(0, Integer::sum));
//        logStatistics.setDistinctWindowsCount(res.size());
//        logStatistics.setTraceVariantsCount(windowLog.getTraceVariantIds().size());
//        logStatistics.setTraceVariantsTotalEvents(this.traceVariantTotalEvents());
//        logStatistics.setActivitiesCount(windowLog.getMapping().getLabelMap().size());
    }

//    private int traceVariantTotalEvents() {
//        int sum = 0;
//        for (Integer id : windowLog.getTraceVariantIds()) {
//            sum += windowLog.getTraceVariant(id).size();
//        }
//        return sum;
//    }

    public LEFRMatrix getLEFRMatrix(int limit) {
        if (lefrMatrix == null || lefrMatrix.getLimit() == limit) {
            lefrMatrix = new LEFRMatrix(eventLog, limit);
        }
        return lefrMatrix;
    }

    public LogStatistics getLogStatistics() {
        return logStatistics;
    }
}
