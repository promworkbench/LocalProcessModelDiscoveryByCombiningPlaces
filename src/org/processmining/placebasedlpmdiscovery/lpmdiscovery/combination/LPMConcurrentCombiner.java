package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.Event;
import org.processmining.placebasedlpmdiscovery.model.lpms.IntermediateLPM;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LPMConcurrentCombiner {

    public LPMConcurrentCombiner() {

    }

    public void buildConcurrency(List<IntermediateLPM> lpms, int cardinality, List<Event> eventSequence) {
        buildConcurrencyRecursive(lpms, new ArrayList<>(lpms), new HashSet<>(lpms), cardinality - 1);
    }

    private void buildConcurrencyRecursive(List<IntermediateLPM> original, List<IntermediateLPM> forExtension,
                                           Set<IntermediateLPM> all, int remainingIterations) {
        if (remainingIterations == 0) {
            return;
        }
        List<IntermediateLPM> nextForExtension = new ArrayList<>();
        for (IntermediateLPM base : forExtension) {
            for (IntermediateLPM extension : original) {

            }
        }
        all.addAll(nextForExtension);
        buildConcurrencyRecursive(original, nextForExtension, all, --remainingIterations);
    }

//    private boolean tobeRemoved(IntermediateLPM lpm1, IntermediateLPM lpm2, Set<LocalProcessModel> all) {
//        LocalProcessModel resLpm = LocalProcessModelUtils.join(lpm1.getLpm(), lpm2.getLpm());
//        if (!all.contains(resLpm)) {
//            List<Integer> sequence = SequenceUtils.joinSubsequences(fsi, fs, window, true);
//            Replayer replayer = new Replayer(resLpm, windowLog.getMapping().getLabelMap());
//            return replayer.canReplay(sequence);
//        }
//    }
}
