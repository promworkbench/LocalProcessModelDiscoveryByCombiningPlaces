package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTraceTransformer;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LPMTemporaryWindowInfoCreator {

    private final IntegerMappedLog log;
    private final SlidingWindowInfo windowInfo;
    private final ActivityBasedTotallyOrderedEventLogTraceVariant parentSequence;

    public LPMTemporaryWindowInfoCreator(SlidingWindowInfo windowInfo, IntegerMappedLog log,
                                         ActivityBasedTotallyOrderedEventLogTraceVariant parentSequence) {
        this.windowInfo = windowInfo;
        this.log = log;
        this.parentSequence = parentSequence;
    }

    public LPMTemporaryWindowInfo createTempInfo(WindowLPMTreeNode n) {
        return new LPMTemporaryWindowInfo(
                n.getLpm().getFiringSequence().stream().map(i -> ActivityCache.getInstance().getActivity(
                        ActivityCache.getInstance().getActivityIdForInt(i))).collect(Collectors.toList()),
                n.getReplayedEventsIndices(),
                windowInfo.getWindow(),
                n.getLpm().getUsedPassages(),
                n.getLpm().getUsedConstraints(),
                this.log.getMapping().getReverseLabelMap(),
                windowInfo.getWindowCount(),
                windowInfo.getEndPos(),
                this.parentSequence,
                this.extractOriginalTraces(this.parentSequence));
    }

    private Set<XTrace> extractOriginalTraces(ActivityBasedTotallyOrderedEventLogTraceVariant parentSequence) {
        return parentSequence.getTraces()
                .stream()
                .map(t -> EventLogTraceTransformer.toXTrace(t))
                .collect(Collectors.toSet());
    }

    public LPMTemporaryWindowInfo createTempInfo(LPMTemporaryWindowInfo tempInfo1,
                                                 LPMTemporaryWindowInfo tempInfo2) {
        List<Integer> replayedEventIndices = new ArrayList<>();
        replayedEventIndices.addAll(tempInfo1.getReplayedEventsIndices()); // add replayed indices from the first lpm
        replayedEventIndices.addAll(tempInfo2.getReplayedEventsIndices()); // add replayed indices from the second lpm
        // Finally keep only distinct and sort indices
        replayedEventIndices = replayedEventIndices.stream().distinct().sorted().collect(Collectors.toList());
        List<Activity> sequence = getFiringSequence(replayedEventIndices, windowInfo.getWindow(),
                tempInfo1.getWindowLastEventPos()); // translate the indices to a firing sequence of activities

        // Collect used passages from both lpms
        Set<Pair<Integer, Integer>> usedPassages = new HashSet<>();
        usedPassages.addAll(tempInfo1.getIntegerUsedPassages());
        usedPassages.addAll(tempInfo2.getIntegerUsedPassages());

        // Collect used places from both lpms
        Set<SimplePlace<Integer>> usedPlaces = new HashSet<>();
        usedPlaces.addAll(tempInfo1.getIntegerUsedPlaces());
        usedPlaces.addAll(tempInfo2.getIntegerUsedPlaces());

        // Create temporary window for the merged lpm. Use the trace info from one of the models since it is window
        // dependent and not model dependent.
        return new LPMTemporaryWindowInfo(
                sequence,
                replayedEventIndices,
                windowInfo.getWindow(),
                usedPassages,
                usedPlaces,
                this.log.getMapping().getReverseLabelMap(),
                tempInfo1.getWindowCount(),
                tempInfo1.getWindowLastEventPos(),
                tempInfo1.getTraceVariant(),
                tempInfo1.getOriginalTraces());
    }

    private List<Activity> getFiringSequence(List<Integer> replayedEventIndices, List<Activity> window,
                                            Integer windowLastEventPos) {
        List<Activity> firingSequence = new ArrayList<>();
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
