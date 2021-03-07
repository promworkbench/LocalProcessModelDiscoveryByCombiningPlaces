package org.processmining.placebasedlpmdiscovery.utils.analysis;

import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FPGrowthStatistics {

    private final UUID executionId;

    private final List<Integer> lpmsInLocalTree;
    private final List<Integer> placesAdded;
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
    }

    public void initializeMainTreeStatistics(MainFPGrowthLPMTree tree) {
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

    public void write(String filename, boolean rewrite) {
        File file = new File("data/statistics/" + filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tAverage Num Places Added In Each Step\tAverage LPMs In Local Tree\t" +
                        "Total number of lpms in Main Tree\tNumber of passed trace variants\tMain Tree average width\t" +
                        "Main Tree average width\tMain Tree average height\tTotal number of Main Tree nodes");
            int sumPlaceAdded = placesAdded.stream().mapToInt(i -> i).sum();
            int sumLpmInLocalTree = lpmsInLocalTree.stream().mapToInt(i -> i).sum();
            pw.println(String.join("\t", String.valueOf(executionId),
                    String.valueOf(placesAdded.size() == 0 ? 0 : sumPlaceAdded / placesAdded.size()),
                    String.valueOf(lpmsInLocalTree.size() == 0 ? 0 : sumLpmInLocalTree / lpmsInLocalTree.size()),
                    String.valueOf(sumLpmInLocalTree),
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
