package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LPMTemporaryWindowInfoCreator {

    private final WindowLog windowLog;
    private final WindowInfo windowInfo;

    public LPMTemporaryWindowInfoCreator(WindowInfo windowInfo, WindowLog windowLog) {
        this.windowInfo = windowInfo;
        this.windowLog = windowLog;
    }

    public LPMTemporaryWindowInfo createTempInfo(WindowLPMTreeNode n) {
        return new LPMTemporaryWindowInfo(
                n.getLpm().getFiringSequence(),
                n.getReplayedEventsIndices(),
                windowInfo.getWindow(),
                n.getLpm().getUsedPassages(),
                this.windowLog.getMapping().getReverseLabelMap(),
                windowInfo.getWindowCount(),
                windowInfo.getTraceVariantId(),
                windowInfo.getEndPos(),
                this.windowLog.getOriginalTraces(windowInfo.getTraceVariantId()));
    }

    public LPMTemporaryWindowInfo createTempInfo(LPMTemporaryWindowInfo tempInfo1,
                                                 LPMTemporaryWindowInfo tempInfo2) {
        List<Integer> replayedEventIndices = new ArrayList<>();
        replayedEventIndices.addAll(tempInfo1.getReplayedEventsIndices());
        replayedEventIndices.addAll(tempInfo2.getReplayedEventsIndices());
        replayedEventIndices = replayedEventIndices.stream().distinct().sorted().collect(Collectors.toList());
        List<Integer> sequence = getFiringSequence(replayedEventIndices, windowInfo.getWindow(),
                tempInfo1.getWindowLastEventPos());

        Set<Pair<Integer, Integer>> usedPassages = new HashSet<>();
        usedPassages.addAll(tempInfo1.getIntegerUsedPassages());
        usedPassages.addAll(tempInfo2.getIntegerUsedPassages());

        return new LPMTemporaryWindowInfo(
                sequence,
                replayedEventIndices,
                windowInfo.getWindow(),
                usedPassages,
                this.windowLog.getMapping().getReverseLabelMap(),
                tempInfo1.getWindowCount(),
                tempInfo1.getTraceVariantId(),
                tempInfo1.getWindowLastEventPos(),
                tempInfo1.getOriginalTraces());
    }

    private List<Integer> getFiringSequence(List<Integer> replayedEventIndices, List<Integer> window, Integer windowLastEventPos) {
        List<Integer> firingSequence = new ArrayList<>();
        int ind = 0;
        for (int i = 0; i < window.size(); ++i) {
            int eventPos = windowLastEventPos - window.size() + i + 1; // the last event inclusive
            if (eventPos == replayedEventIndices.get(ind)) {
                firingSequence.add(window.get(i));
                ind++;
            }
            if (replayedEventIndices.size() == ind) {
                break;
            }
        }
        if (ind != replayedEventIndices.size()) {
            throw new IllegalStateException("Something went wrong.");
        }
        return firingSequence;
    }
}
