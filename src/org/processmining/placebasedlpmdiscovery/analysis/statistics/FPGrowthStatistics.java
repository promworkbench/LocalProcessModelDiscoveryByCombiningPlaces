package org.processmining.placebasedlpmdiscovery.analysis.statistics;

import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class FPGrowthStatistics implements IStatistics {

    private final UUID executionId;

    private final List<Integer> lpmsInLocalTree;
    private final List<Integer> placesAdded;
    private final List<Long> windowProcessingDurations;
    private int traceVariantsPassed;

    // main tree statistics
    private double mainTreeAverageNumChildren;
    private int mainTreeMaximalHeight;
    private int mainTreeNodes;
    private int mainTreeMaximalNumChildren;

    public FPGrowthStatistics(UUID executionId) {
        this.executionId = executionId;
        lpmsInLocalTree = new ArrayList<>();
        placesAdded = new ArrayList<>();
        windowProcessingDurations = new ArrayList<>();
    }

    public void initializeMainTreeStatistics(MainFPGrowthLPMTree tree) {
        // list of children of each node
        List<Integer> list = tree
                .getNodes()
                .stream()
                .mapToInt(n -> n.getChildren().size())
                .filter(x -> x != 0)
                .boxed()
                .collect(Collectors.toList());
        this.mainTreeAverageNumChildren = list.stream().mapToInt(x -> x).sum() * 1.0 / list.size();
        this.mainTreeMaximalNumChildren = list.stream().mapToInt(x -> x).max().orElse(-1);
        this.mainTreeMaximalHeight = tree.getHeight();
        this.mainTreeNodes = tree.getNodes().size();
    }

    public void lpmsAddedInMainTree(int count) {
        lpmsInLocalTree.add(count);
    }

    public void placesAddedInLocalTree(int count) {
        placesAdded.add(count);
    }

    public void logWindowProcessed(long duration) {
        windowProcessingDurations.add(duration);
    }

    @Override
    public void write(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tAverage Num Places Added In Each Step\tMin Num Places Added In Each Step\tMax Num Places Added In Each Step\t" +
                        "Average LPMs In Local Tree\tMin LPMs In Local Tree\tMax LPMs In Local Tree\t" +
                        "Average Window Processing Time\tMin Window Processing Time\tMax Window Processing Time\t" +
                        "Number of passed trace variants\tMain Tree average num children\tMain Tree max num children\t" +
                        "Main Tree average height\tTotal number of Main Tree nodes");
            Supplier<IntStream> placeAddedStream = () -> placesAdded.stream().mapToInt(i -> i);
            Supplier<IntStream> lpmInLocalTreeStream = () -> lpmsInLocalTree.stream().mapToInt(i -> i);
            Supplier<LongStream> windowDurationStream = () -> windowProcessingDurations.stream().mapToLong(i -> i);
            pw.println(String.join("\t", String.valueOf(executionId), // execution id
                    String.valueOf(placesAdded.size() == 0 ? 0 : placeAddedStream.get().sum() / placesAdded.size()), // avg places added in each step
                    String.valueOf(placeAddedStream.get().min().orElse(-1)), // min places added in each step
                    String.valueOf(placeAddedStream.get().max().orElse(-1)), // max places added in each step
                    String.valueOf(lpmsInLocalTree.size() == 0 ? 0 : lpmInLocalTreeStream.get().sum() / lpmsInLocalTree.size()), // avg lpms in local tree
                    String.valueOf(lpmInLocalTreeStream.get().min().orElse(-1)), // min lpms in local tree
                    String.valueOf(lpmInLocalTreeStream.get().max().orElse(-1)), // max lpms in local tree
                    String.valueOf(windowProcessingDurations.size() == 0 ? 0 : windowDurationStream.get().sum() / windowProcessingDurations.size()), // avg window duration
                    String.valueOf(windowDurationStream.get().min().orElse(-1)), // min window duration
                    String.valueOf(windowDurationStream.get().max().orElse(-1)), // max window duration
                    String.valueOf(traceVariantsPassed),
                    String.valueOf(mainTreeAverageNumChildren),
                    String.valueOf(mainTreeMaximalNumChildren),
                    String.valueOf(mainTreeMaximalHeight),
                    String.valueOf(mainTreeNodes)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void traceVariantPassed() {
        this.traceVariantsPassed++;
    }
}
