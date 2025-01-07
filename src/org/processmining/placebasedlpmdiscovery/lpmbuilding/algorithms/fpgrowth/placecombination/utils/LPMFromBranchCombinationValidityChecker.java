package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.utils;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LPMFromBranchCombinationValidityChecker {


    private final Map<String, Integer> labelMapping;

    public LPMFromBranchCombinationValidityChecker(Map<String, Integer> labelMapping) {
        this.labelMapping = labelMapping;
    }

    public boolean checkValidity(LocalProcessModel lpm, LPMTemporaryWindowInfo tempInfo) {
        Replayer replayer = new Replayer(lpm, labelMapping);
        return replayer.canReplayActivitySequence(tempInfo.getActivityFiringSequence());
    }

    public boolean shouldMerge(LPMTemporaryWindowInfo tempInfo1, LPMTemporaryWindowInfo tempInfo2) {
        List<Activity> fs1 = tempInfo1.getActivityFiringSequence();
        List<Activity> fs2 = tempInfo2.getActivityFiringSequence();
        return fs2.stream().anyMatch(fs1::contains)
                && !new HashSet<>(fs1).containsAll(fs2)
                && !new HashSet<>(fs2).containsAll(fs1);
    }
}
