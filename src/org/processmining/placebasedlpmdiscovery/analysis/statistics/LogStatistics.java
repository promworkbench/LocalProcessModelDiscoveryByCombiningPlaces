package org.processmining.placebasedlpmdiscovery.analysis.statistics;

import com.google.common.base.Strings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.UUID;

public class LogStatistics implements IStatistics {

    private final UUID executionId;

    private String logName;
    private Integer allWindowsCount;
    private int distinctWindowsCount;
    private int traceVariantsCount;
    private int traceVariantsTotalEvents;
    private int activitiesCount;
    private int windowSize;

    public LogStatistics() {
        this.executionId = UUID.randomUUID();
    }

    public LogStatistics(UUID executionId) {
        this.executionId = executionId;
    }

    @Override
    public void write(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tEvent Log Name\tTrace Variant (tv) Count\tActivities Count\ttv Sum\ttv Average Size" +
                        "\tWindow size\tAll windows count\tDistinct windows count");
            pw.println(String.join("\t", String.valueOf(executionId), // id
                    String.valueOf(logName), // event log name
                    String.valueOf(traceVariantsCount), // trace variant count
                    String.valueOf(activitiesCount), // activities count
                    String.valueOf(traceVariantsTotalEvents), // trace variant total events
                    String.valueOf(traceVariantsTotalEvents / traceVariantsCount),
                    String.valueOf(windowSize),
                    String.valueOf(allWindowsCount),
                    String.valueOf(distinctWindowsCount)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAllWindowsCount(Integer allWindowsCount) {
        this.allWindowsCount = allWindowsCount;
    }

    public void setDistinctWindowsCount(int distinctWindowsCount) {
        this.distinctWindowsCount = distinctWindowsCount;
    }

    public void setTraceVariantsCount(int traceVariantsCount) {
        this.traceVariantsCount = traceVariantsCount;
    }

    public void setTraceVariantsTotalEvents(int traceVariantsTotalEvents) {
        this.traceVariantsTotalEvents = traceVariantsTotalEvents;
    }

    public void setActivitiesCount(int activitiesCount) {
        this.activitiesCount = activitiesCount;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public void setLogName(String logName) {
        if (Strings.isNullOrEmpty(logName)) {
            this.logName = UUID.randomUUID().toString();
        } else {
            this.logName = logName;
        }
    }

    public String getLogName() {
        return logName;
    }

    public Integer getAllWindowsCount() {
        return allWindowsCount;
    }

    public int getDistinctWindowsCount() {
        return distinctWindowsCount;
    }

    public int getTraceVariantsCount() {
        return traceVariantsCount;
    }

    public int getTraceVariantsTotalEvents() {
        return traceVariantsTotalEvents;
    }

    public int getActivitiesCount() {
        return activitiesCount;
    }

    public int getWindowSize() {
        return windowSize;
    }
}
