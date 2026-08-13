package org.processmining.lpms.quality.alignments.tax;

import nl.tue.astar.AStarException;
import nl.tue.astar.Trace;
import nl.tue.astar.impl.DijkstraTail;
import org.processmining.lpm.adjustedalignments.AStarThread.ASynchronousMoveSorting;
import org.processmining.lpm.adjustedalignments.AllSamplingOptAlignmentsGraphThread;
import org.processmining.lpm.adjustedalignments.MemoryEfficientAStarAlgorithm;
import org.processmining.lpm.adjustedalignments.NBestOptAlignmentsNoModelMoveGraphSamplingAlg;
import org.processmining.lpm.adjustedalignments.PHead;

/**
 * Drop-in replacement for {@link NBestOptAlignmentsNoModelMoveGraphSamplingAlg} that recovers
 * optimal alignments otherwise silently dropped due to unrecorded ties during search.
 * <p>
 * See {@link TieCapturingSamplingThread} for the actual fix; this class only swaps in that
 * patched thread, so {@code replayLog}/{@code recordToResult} run completely unmodified.
 */
public class TieAwareNBestAlignmentsAlg extends NBestOptAlignmentsNoModelMoveGraphSamplingAlg {

    @Override
    protected AllSamplingOptAlignmentsGraphThread<PHead, DijkstraTail> getThread(
            MemoryEfficientAStarAlgorithm<PHead, DijkstraTail> aStar, PHead initial, Trace trace,
            int maxNumOfStates) throws AStarException {
        AllSamplingOptAlignmentsGraphThread<PHead, DijkstraTail> thread =
                new TieCapturingSamplingThread(aStar, initial, trace, maxNumOfStates);
        thread.setASynchronousMoveSorting(ASynchronousMoveSorting.LOGMOVEFIRST);
        return thread;
    }
}
