package org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LEFRMatrix {

    private final XLog log;
    private final int limit;

    private Map<String, Integer> nameIndexMap;
    private int[][] lefr;

    public LEFRMatrix(XLog log, int limit) {
        this.log = log;
        this.limit = limit;
        this.calculateMatrix();
    }

    public void initializeNameIndexMap() {
        nameIndexMap = new HashMap<>();
        AtomicInteger ind = new AtomicInteger(0);
        Collection<String> activities = LogUtils.getActivitiesFromLog(log);
        activities.forEach(a -> nameIndexMap.put(a, ind.getAndIncrement()));
    }

    public int getRowSize() {
        return lefr.length;
    }

    public int getColumnSize() {
        return lefr.length > 0 ? lefr[0].length : 0;
    }

    public int get(int row, int col) {
        return lefr[row][col];
    }

    public int get(String rowName, String colName) {
        if (!nameIndexMap.containsKey(rowName) || !nameIndexMap.containsKey(colName))
            return 0;
        return get(nameIndexMap.get(rowName), nameIndexMap.get(colName));
    }

    public void calculateMatrix() {
        this.initializeNameIndexMap();
        lefr = new int[nameIndexMap.size()][nameIndexMap.size()];
        for (XTrace trace : log) {
            List<String> events = trace
                    .stream()
                    .map(event -> ((XAttributeLiteral) event.getAttributes().get(XConceptExtension.KEY_NAME)).getValue())
                    .collect(Collectors.toList());
            for (int i = 1; i < events.size(); ++i) {
                for (int j = Math.max(0, i + 1 - this.limit); j < i; ++j) {
                    lefr[nameIndexMap.get(events.get(j))][nameIndexMap.get(events.get(i))]++;
                }
            }
        }
    }

    public Map<String, Integer> getNameIndexMap() {
        if (nameIndexMap == null)
            initializeNameIndexMap();
        return nameIndexMap;
    }

    public int getLimit() {
        return limit;
    }
}
