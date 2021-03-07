package org.processmining.placebasedlpmdiscovery.utils.analysis;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LogStatistics {

    private final UUID executionId;

    private int traceVariantsCount;
    private int activitiesCount;
    private int traceVariantsSizeSum;
    private int traceVariantsMinSize;
    private int traceVariantsMaxSize;
    private int traceVariantsMedianSize;

    public LogStatistics(UUID executionId, XLog log) {
        this.executionId = executionId;
        this.initialize(log);
    }

    private void initialize(XLog log) {
        List<Integer> sizes = log.stream().map(List::size).collect(Collectors.toList());
        this.traceVariantsCount = log.size();
        this.traceVariantsMaxSize = Collections.max(sizes);
        this.traceVariantsMinSize = Collections.min(sizes);
        this.traceVariantsSizeSum = sizes.stream().reduce(0, Integer::sum);
        Collections.sort(sizes);
        this.traceVariantsMedianSize = sizes.size() % 2 != 0 ?
                sizes.get(sizes.size() / 2) :
                (sizes.get(sizes.size() / 2) + sizes.get(sizes.size() / 2 - 1)) / 2;
        this.activitiesCount = LogUtils.getActivitiesFromLog(log).size();
    }

    public void write(String filename, boolean rewrite) {
        File file = new File("data/statistics/" + filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tTrace Variant (tv) Count\tActivities Count\ttv Sum\t" +
                        "tv Min Size\ttv Max Size\ttv Average Size\ttv Median Size");
            pw.println(String.join("\t", String.valueOf(executionId),
                    String.valueOf(traceVariantsCount),
                    String.valueOf(activitiesCount),
                    String.valueOf(traceVariantsSizeSum),
                    String.valueOf(traceVariantsMinSize),
                    String.valueOf(traceVariantsMaxSize),
                    String.valueOf(traceVariantsSizeSum / traceVariantsCount),
                    String.valueOf(traceVariantsMedianSize)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
