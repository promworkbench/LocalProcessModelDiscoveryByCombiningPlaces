package org.processmining.placebasedlpmdiscovery.replayer;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Replayer {

    private final Map<LinkedList<Integer>, Boolean> sequenceCanBeReplayedMap;

    private final LocalProcessModel lpm;
    private final Map<String, Integer> labelMapping;

    public Replayer(LocalProcessModel lpm, Map<String, Integer> labelMapping) {
        this.sequenceCanBeReplayedMap = new HashMap<>();
        this.lpm = lpm;
        this.labelMapping = labelMapping;
    }

    public boolean canReplay(LinkedList<Integer> sequence, ReplayerType type) {
        Boolean canReplay = this.sequenceCanBeReplayedMap.getOrDefault(sequence, null);
        if (canReplay != null)
            return canReplay;

        if (type == ReplayerType.PREFIX)
            canReplay = canReplayPrefix(sequence);

        if (type == ReplayerType.SUFFIX)
            canReplay = canReplaySuffix(sequence);

        if (type == ReplayerType.BOTH)
            canReplay = canReplayPrefix(sequence) || canReplaySuffix(sequence);

        this.sequenceCanBeReplayedMap.put(sequence, canReplay);
        return canReplay != null ? canReplay : false;
    }

    private boolean canReplaySuffix(LinkedList<Integer> sequence) {
        LinkedList<Integer> reversed = new LinkedList<>(sequence);
        Collections.reverse(reversed);
        return getLPMAfterReplay(reversed, LocalProcessModelUtils.convertToReplayable(
                LocalProcessModelUtils.revertLocalProcessModel(this.lpm), this.labelMapping)) != null;
    }

    private boolean canReplayPrefix(LinkedList<Integer> sequence) {
        return getLPMAfterReplay(sequence, LocalProcessModelUtils.convertToReplayable(this.lpm, this.labelMapping)) != null;
    }

    /**
     * Replays the sequence on the local process model
     *
     * @param sequence: the sequence that is replayed
     * @return a sequence of transitions that have been fired
     */
    public List<Integer> replay(LinkedList<Integer> sequence, ReplayerType type) {
        List<Integer> replayedSequence = null;
        if (type == ReplayerType.PREFIX)
            replayedSequence = replayPrefix(sequence);

        if (type == ReplayerType.SUFFIX)
            replayedSequence = replaySuffix(sequence);

        if (type == ReplayerType.BOTH) {
            replayedSequence = replayPrefix(sequence);
            if (replayedSequence == null)
                replayedSequence = replaySuffix(sequence);
        }

        return replayedSequence;
    }

    private List<Integer> replayPrefix(LinkedList<Integer> sequence) {
        ReplayableLocalProcessModel lpm = getLPMAfterReplay(sequence, LocalProcessModelUtils
                .convertToReplayable(this.lpm, this.labelMapping));
        return lpm != null ? lpm.getFiringSequence() : null;
    }

    private List<Integer> replaySuffix(LinkedList<Integer> sequence) {
        LinkedList<Integer> reversed = new LinkedList<>(sequence);
        Collections.reverse(reversed);
        ReplayableLocalProcessModel lpm = getLPMAfterReplay(reversed, LocalProcessModelUtils
                .convertToReplayable(LocalProcessModelUtils.revertLocalProcessModel(this.lpm), this.labelMapping));
        List<Integer> reversedFiringSequence = lpm != null ? lpm.getFiringSequence() : null;
        if (reversedFiringSequence != null)
            Collections.reverse(reversedFiringSequence);
        return reversedFiringSequence;
    }

    private ReplayableLocalProcessModel getLPMAfterReplay(LinkedList<Integer> sequence, ReplayableLocalProcessModel replayable) {
        Set<Integer> visitedHashCodes = new HashSet<>(); // we need to make sure we do not have infinite loops
        // TODO: try to find the best replay
        //  ReplayableLocalProcessModel currentBest = null;

        Queue<Pair<ReplayableLocalProcessModel, LinkedList<Integer>>> queue = new LinkedList<>();
        queue.add(new Pair<>(replayable, sequence));
        while (!queue.isEmpty()) {
            Pair<ReplayableLocalProcessModel, LinkedList<Integer>> current = queue.poll();
            ReplayableLocalProcessModel model = current.getKey();
            LinkedList<Integer> window = current.getValue();

            if (visitedHashCodes.contains(Objects.hash(model.getConstraintMap(), window)))
                continue;

            visitedHashCodes.add(Objects.hash(model.getConstraintMap(), window));

            Set<Integer> enabled = model.getEnabledInvisible();
            if (!window.isEmpty() && model.canFire(window.getFirst())) // TODO: Should be reimplemented in order to support duplicate transitions
                enabled.add(window.getFirst());

            for (int enabledTr : enabled) {
                ReplayableLocalProcessModel copyReplayable = new ReplayableLocalProcessModel(model);
                LinkedList<Integer> copyWindow = new LinkedList<>(window);

                boolean fired = copyReplayable.fire(enabledTr);
                if (fired && !copyWindow.isEmpty() && enabledTr == copyWindow.getFirst()) { // if we have fired visible transitions
                    copyWindow.removeFirst(); // remove it from the sequence we need to replay
                }

                if (copyReplayable.hasFired() && copyReplayable.isEmptyMarking()) { // if has fired and the marking is empty
                    return copyReplayable;
                }

                if (!visitedHashCodes.contains(Objects.hash(copyReplayable.getConstraintMap(), copyWindow))
                        && copyReplayable.getSilentFiresCount() < 5)
                    queue.add(new Pair<>(copyReplayable, copyWindow));
            }

        }
        return null;
    }

    public boolean canReplay(List<Integer> sequence) {
        return canReplay(sequence, LocalProcessModelUtils
                .convertToReplayable(this.lpm, this.labelMapping));
    }

    public boolean canReplay(List<Integer> sequence, ReplayableLocalProcessModel rlpm) {
        for (Integer event : sequence) {
            if (rlpm.canFire(event))
                rlpm.fire(event);
            else
                return false;
        }
        return rlpm.isEmptyMarking();
    }

    public enum ReplayerType {
        PREFIX,
        SUFFIX,
        BOTH
    }

    // TODO: Make one normal replay where the sequence is a list of strings

    public static Set<List<String>> findAllPaths(int pathLengthLimit, LocalProcessModel lpm) {
        Set<List<String>> paths = new HashSet<>();

        Map<String, Integer> transitionLabelToIntegerMap = LocalProcessModelUtils.getTransitionLabelToIntegerMap(lpm);
        Map<Integer, String> integerToTransitionLabelMap = transitionLabelToIntegerMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        ReplayableLocalProcessModel rlpm = LocalProcessModelUtils.convertToReplayableWithInitialMarking(
                lpm, transitionLabelToIntegerMap);
        Set<Integer> unconstrainedTransitions = rlpm.getEnabledTransitions();

        Queue<ReplayableLocalProcessModel> queue = new LinkedList<>();
        queue.add(rlpm);
        while (!queue.isEmpty()) {
            // poll the next lpm to be processed
            ReplayableLocalProcessModel current = queue.poll();

            // if empty and has fired add it to possible paths
            if (current.isEmptyMarking() && !current.getFiringSequence().isEmpty()) {
                paths.add(current.getFiringSequence().stream().map(integerToTransitionLabelMap::get).collect(Collectors.toList()));
//                continue;
            }

            // if the maximum path length limit is achieved, don't continue with replay
            if (current.getFiringSequence().size() >= pathLengthLimit) {
                continue;
            }

            for (Integer tr : current.getEnabledTransitions()) {
//                // unconstrained transitions can fire once
//                if (unconstrainedTransitions.contains(tr) && current.getFiringSequence().contains(tr)) {
//                    continue;
//                }
                ReplayableLocalProcessModel copy = new ReplayableLocalProcessModel(current);
                copy.fire(tr);
                queue.add(copy);
            }
        }

        return paths;
    }
}
