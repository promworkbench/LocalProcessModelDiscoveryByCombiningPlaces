package org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.analysis;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.fpgrowth.MainFPGrowthLPMTree;

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
        this.mainTreeMaximalHeight = tree.getHeight();
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
                        "Number of passed trace variants\tMain tree width\tMain tree height");
            int sumPlaceAdded = placesAdded.stream().mapToInt(i -> i).sum();
            int sumLpmInLocalTree = lpmsInLocalTree.stream().mapToInt(i -> i).sum();
            pw.println(String.join("\t", String.valueOf(executionId),
                    String.valueOf(placesAdded.size() == 0 ? 0 : sumPlaceAdded / placesAdded.size()),
                    String.valueOf(lpmsInLocalTree.size() == 0 ? 0 : sumLpmInLocalTree / lpmsInLocalTree.size()),
                    String.valueOf(traceVariantsPassed),
                    String.valueOf(mainTreeAverageNumChildren),
                    String.valueOf(mainTreeMaximalHeight)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void traceVariantPassed() {
        this.traceVariantsPassed++;
    }
}
