package org.processmining.lpms.quality.alignments;

import nl.tue.astar.*;
import nl.tue.astar.impl.DijkstraTail;
import org.processmining.lpm.adjustedalignments.AllSamplingOptAlignmentsGraphThread;
import org.processmining.lpm.adjustedalignments.MemoryEfficientAStarAlgorithm;
import org.processmining.lpm.adjustedalignments.PHead;
import org.processmining.lpm.adjustedalignments.PRecord;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Patched search thread that recovers optimal alignments the LocalProcessModelDiscovery library silently drops.
 * No changes to {@code NBestOptAlignmentsNoModelMoveGraphSamplingAlg} are required; see
 * {@link TieAwareNBestAlignmentsAlg} for the (only) integration point.
 */
public class TieCapturingSamplingThread extends AllSamplingOptAlignmentsGraphThread.MemoryEfficient<PHead,
        DijkstraTail> {

    private final Map<Record, List<PRecord>> altArrivalsByWinner = new IdentityHashMap<>();

    public TieCapturingSamplingThread(MemoryEfficientAStarAlgorithm<PHead, DijkstraTail> algorithm, PHead initialHead,
                                      Trace trace, int maxStates) throws AStarException {
        super(algorithm, initialHead, trace, maxStates);
        this.mapToStatesWSameSuffix = new IdentityHashMap<>();
        this.addObserver(new TieGraftingObserver());
    }

    /**
     * Overriding the processMove to store the alternative records that arrive at a state that is already present in
     * the queue. See {@link #altArrivalsByWinner} for more details.
     */
    @Override
    protected void processMove(PHead head, DijkstraTail tail, Record rec, int modelMove, int movedEvent, int activity)
            throws AStarException {
        PHead newHead = computeNextHead(rec, head, modelMove, movedEvent, activity);
        long index = storageHandler.getIndexOf(newHead);
        if (index >= 0) {
            Record newRec = rec.getNextRecord(delegate, trace, newHead, index, modelMove, movedEvent, activity);
            Record existing = queue.contains(newRec);
            if (existing != null && existing.getCostSoFar() == newRec.getCostSoFar()) {
                List<PRecord> ties = altArrivalsByWinner.computeIfAbsent(existing, k -> new ArrayList<>());
                ties.add((PRecord) newRec);
            }
        }
        super.processMove(head, tail, rec, modelMove, movedEvent, activity);
    }

    /**
     * Builds every alternate {@link PRecord} chain reaching {@code finalRecord}'s own state,
     * combinatorially. For each position along the winning, takes the possible alternatives
     * and appends the winning history from that position forward.
     */
    private List<PRecord> buildAlternates(PRecord finalRecord) {
        List<PRecord> history = PRecord.getHistory(finalRecord);
        List<PRecord> alternates = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) { // iterate the history from the source towards the final record
            // find all possible alternates at position i and graft them the winning suffix
            for (PRecord alt : alternatesAt(history.get(i))) {
                alternates.add(graftSuffix(alt, history, i + 1));
            }
        }
        return alternates;
    }

    /**
     * All complete record chains that reach the same (state, cost) as {@code winner}.
     */
    private List<PRecord> alternatesAt(PRecord winner) {
        List<PRecord> ties = altArrivalsByWinner.get(winner);
        List<PRecord> result = new ArrayList<>();
        if (ties == null) {
            return result;
        }
        for (PRecord alt : ties) { // for each alternative
            result.add(alt); // add it as an alternative to winner
            List<PRecord> altHistory = PRecord.getHistory(alt);
            for (int j = 0; j < altHistory.size(); j++) {
                // check if there are alternatives to the alternative's history
                for (PRecord nested : alternatesAt(altHistory.get(j))) {
                    // graft the winning suffix onto the nested alternative and add it to the result.
                    // Note: the winning suffix always ends with the winner, not the final state.
                    result.add(graftSuffix(nested, altHistory, j + 1));
                }
            }
        }
        return result;
    }

    /**
     * Replays {@code history[fromIndex..]}'s moves on top of {@code prefix}, producing a new
     * {@link PRecord} chain that ends at the same state {@code history}'s own last element does.
     *
     * @param prefix    is in a way linked list holding the entire history up to this point and is specifically the
     *                  place where two divergent branches met
     * @param history   is the list of records from the winning branch starting with the initial and ending with the
     *                  final state
     * @param fromIndex is the index in the history list from which to start grafting, denoting the first common record
     * @return an alternative execution path that shares the same suffix as the winning path, but diverges before the
     * point of the tie
     */
    private PRecord graftSuffix(PRecord prefix, List<PRecord> history, int fromIndex) {
        PRecord graft = prefix;
        for (int j = fromIndex; j < history.size(); j++) {
            PRecord origStep = history.get(j);
            graft = new PRecord(origStep.getState(), 0, graft, origStep.getMovedEvent(), origStep.getModelMove(),
                    0, graft.getBacktraceSize() + 1, null);
        }
        return graft;
    }

    private class TieGraftingObserver implements AStarObserver {
        @Override
        public void finalNodeFound(Record node) {
            List<PRecord> alternates = buildAlternates((PRecord) node);
            if (!alternates.isEmpty()) {
                mapToStatesWSameSuffix.put(node, new ArrayList<>(alternates));
            }
        }

        @Override
        public void nodeVisited(Record node) {
        }

        @Override
        public void edgeTraversed(Record from, Record to) {
        }

        @Override
        public void estimateComputed(Head head) {
        }

        @Override
        public void initialNodeCreated(Record node) {
        }

        @Override
        public void stoppedUnreliablyAt(Record rec) {
        }

        @Override
        public void close() {
        }
    }
}
