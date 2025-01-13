package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.utils;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.replayer.ReplayerForReplayableLocalProcessModel;

import java.util.HashSet;
import java.util.List;

public class LPMFromBranchCombinationValidityChecker {


    public LPMFromBranchCombinationValidityChecker() {
    }

    public boolean checkValidity(LocalProcessModel lpm, LPMTemporaryWindowInfo tempInfo) {
        ReplayerForReplayableLocalProcessModel replayer = new ReplayerForReplayableLocalProcessModel(lpm);
        return replayer.canReplayActivitySequence(tempInfo.getActivityFiringSequence());
    }

    public boolean checkValidity(ReplayableLocalProcessModel lpm, List<Activity> firingSequence) {
        ReplayerForReplayableLocalProcessModel replayer = new ReplayerForReplayableLocalProcessModel(lpm);
        return replayer.canReplayActivitySequence(firingSequence);
    }

    public boolean shouldNotMerge(LPMTemporaryWindowInfo tempInfo1, LPMTemporaryWindowInfo tempInfo2) {
        return shouldNotMerge(tempInfo1.getActivityFiringSequence(), tempInfo2.getActivityFiringSequence());
    }

    public <T> boolean shouldNotMerge(List<T> firingSeq1, List<T> firingSeq2) {
        return firingSeq2.stream().noneMatch(firingSeq1::contains)
                || new HashSet<>(firingSeq1).containsAll(firingSeq2)
                || new HashSet<>(firingSeq2).containsAll(firingSeq1);
    }
}
