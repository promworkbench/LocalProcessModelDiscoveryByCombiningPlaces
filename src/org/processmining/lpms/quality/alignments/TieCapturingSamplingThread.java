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
 * Patched search thread that recovers optimal alignments the upstream
 * LocalProcessModelDiscovery library silently drops.
 * <p>
 * Root cause (see memory/nbest_alignment_tie_collapse.md for the full traced evidence):
 * {@code AbstractAStarThreadNoModelMoves#processMove} discards any arrival that ties in
 * cost with a record already occupying the same (marking, parikh) state, without recording
 * the discarded alternative anywhere. {@code mapToStatesWSameSuffix} exists for exactly this
 * purpose but its populating logic ships commented out upstream. This class revives that
 * intent, generalized to ties at ANY state (not just the final one):
 * <ol>
 *   <li>{@link #processMove} records each discarded tie, keyed by the IDENTITY of the record
 *       that won the tie - not by state, since many distinct optimal alignments legitimately
 *       share a terminal state (every completed alignment for a trace converges on the same
 *       final marking/empty-parikh state), and a state-keyed/equals-based map would let a
 *       later-found final silently overwrite an earlier one's recorded ties.</li>
 *   <li>On {@code finalNodeFound}, {@link #buildAlternates} walks the winning chain and, for
 *       every step that was itself once a tie-winner, grafts the discarded alternative's own
 *       (already-correct) prefix onto a replay of the winning chain's remaining suffix -
 *       producing a full alternate {@link PRecord} chain for the alignment that would
 *       otherwise be lost.</li>
 *   <li>The constructor swaps the inherited {@code mapToStatesWSameSuffix} (normally a
 *       state-keyed {@code HashMap}) for an {@code IdentityHashMap}, so the unmodified
 *       {@code recordToResult}/{@code replayLog} lookups (keyed by the specific final record
 *       instance) can't collide across distinct finals that share a state either.</li>
 * </ol>
 * No changes to {@code NBestOptAlignmentsNoModelMoveGraphSamplingAlg} are required; see
 * {@link TieAwareNBestAlignmentsAlg} for the (only) integration point.
 */
public class TieCapturingSamplingThread extends AllSamplingOptAlignmentsGraphThread.MemoryEfficient<PHead,
        DijkstraTail> {

    private final Map<Record, List<PRecord>> altArrivalsByWinner = new IdentityHashMap<Record, List<PRecord>>();

    public TieCapturingSamplingThread(MemoryEfficientAStarAlgorithm<PHead, DijkstraTail> algorithm, PHead initialHead,
                                      Trace trace, int maxStates) throws AStarException {
        super(algorithm, initialHead, trace, maxStates);
        this.mapToStatesWSameSuffix = new IdentityHashMap<Record, List<Record>>();
        this.addObserver(new TieGraftingObserver());
    }

    @Override
    protected void processMove(PHead head, DijkstraTail tail, Record rec, int modelMove, int movedEvent, int activity)
            throws AStarException {
        PHead newHead = computeNextHead(rec, head, modelMove, movedEvent, activity);
        long index = storageHandler.getIndexOf(newHead);
        if (index >= 0) {
            Record newRec = rec.getNextRecord(delegate, trace, newHead, index, modelMove, movedEvent, activity);
            Record existing = queue.contains(newRec);
            if (existing != null && existing.getCostSoFar() == newRec.getCostSoFar()) {
                List<PRecord> ties = altArrivalsByWinner.computeIfAbsent(existing, k -> new ArrayList<PRecord>());
                ties.add((PRecord) newRec);
            }
        }
        super.processMove(head, tail, rec, modelMove, movedEvent, activity);
    }

    /**
     * Grafts one alternate {@link PRecord} chain per discarded tie found anywhere along
     * {@code finalRecord}'s own winning history: the discarded arrival's real prefix, followed
     * by a replay of whatever moves the winning chain took after the collision point.
     */
    private List<PRecord> buildAlternates(PRecord finalRecord) {
        List<PRecord> history = PRecord.getHistory(finalRecord);
        List<PRecord> alternates = new ArrayList<PRecord>();
        for (int i = 0; i < history.size(); i++) {
            List<PRecord> ties = altArrivalsByWinner.get(history.get(i));
            if (ties == null) {
                continue;
            }
            for (PRecord altArrival : ties) {
                PRecord graft = altArrival;
                for (int j = i + 1; j < history.size(); j++) {
                    PRecord origStep = history.get(j);
                    // cost/markingsize/executed are never read by PRecord.getHistory or
                    // AllOptAlignmentsGraphSamplingAlg#constructResult - only predecessor,
                    // modelMove and movedEvent matter for reconstructing the alignment.
                    graft = new PRecord(origStep.getState(), 0, graft, origStep.getMovedEvent(),
                            origStep.getModelMove(), 0, graft.getBacktraceSize() + 1, null);
                }
                alternates.add(graft);
            }
        }
        return alternates;
    }

    private class TieGraftingObserver implements AStarObserver {
        @Override
        public void finalNodeFound(Record node) {
            List<PRecord> alternates = buildAlternates((PRecord) node);
            if (!alternates.isEmpty()) {
                mapToStatesWSameSuffix.put(node, new ArrayList<Record>(alternates));
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
