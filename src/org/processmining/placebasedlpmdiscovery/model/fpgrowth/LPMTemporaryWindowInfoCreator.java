package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LPMTemporaryWindowInfoCreator {

    private final IntegerMappedLog log;
    private final WindowInfo windowInfo;

    public LPMTemporaryWindowInfoCreator(WindowInfo windowInfo, IntegerMappedLog log) {
        this.windowInfo = windowInfo;
        this.log = log;
    }

    public LPMTemporaryWindowInfo createTempInfo(WindowLPMTreeNode n) {
        return new LPMTemporaryWindowInfo(
                n.getLpm().getFiringSequence(),
                n.getReplayedEventsIndices(),
                windowInfo.getWindow(),
                n.getLpm().getUsedPassages(),
                n.getLpm().getUsedConstraints(),
                this.log.getMapping().getReverseLabelMap(),
                windowInfo.getWindowCount(),
                windowInfo.getTraceVariantId(),
                windowInfo.getEndPos(),
                this.log.getOriginalTraces(windowInfo.getTraceVariantId()));
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

        Set<SimplePlace<Integer>> usedPlaces = new HashSet<>();
        usedPlaces.addAll(tempInfo1.getIntegerUsedPlaces());
        usedPlaces.addAll(tempInfo2.getIntegerUsedPlaces());

        return new LPMTemporaryWindowInfo(
                sequence,
                replayedEventIndices,
                windowInfo.getWindow(),
                usedPassages,
                usedPlaces,
                this.log.getMapping().getReverseLabelMap(),
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
