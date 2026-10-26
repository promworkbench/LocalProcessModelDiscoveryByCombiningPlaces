package org.processmining.lpms.quality.alignments;

import nl.tue.astar.*;
import nl.tue.astar.impl.DijkstraTail;
import org.processmining.lpm.adjustedalignments.AllSamplingOptAlignmentsGraphThread;
import org.processmining.lpm.adjustedalignments.MemoryEfficientAStarAlgorithm;
import org.processmining.lpm.adjustedalignments.NBestOptAlignmentsNoModelMoveGraphSamplingAlg;
import org.processmining.lpm.adjustedalignments.PHead;

/**
 * Debug-only subclass that attaches a printing {@link AStarObserver} to the search
 * thread before it starts polling, so every visited/queued/finalized state during
 * replayLog() is printed instead of requiring interactive debugger step-through.
 */
public class DebuggingNBestAlg extends NBestOptAlignmentsNoModelMoveGraphSamplingAlg {

    @Override
    protected AllSamplingOptAlignmentsGraphThread<PHead, DijkstraTail> getThread(
            MemoryEfficientAStarAlgorithm<PHead, DijkstraTail> aStar, PHead initial, Trace trace,
            int maxNumOfStates) throws AStarException {
        AllSamplingOptAlignmentsGraphThread<PHead, DijkstraTail> thread =
                super.getThread(aStar, initial, trace, maxNumOfStates);
        thread.addObserver(new PrintingObserver());
        return thread;
    }

    private static class PrintingObserver implements AStarObserver {
        private int visited = 0;
        private int edges = 0;

        @Override
        public void nodeVisited(Record node) {
            visited++;
            System.out.println("VISIT  #" + visited
                    + " state=" + node.getState()
                    + " costSoFar=" + node.getCostSoFar()
                    + " totalCost=" + node.getTotalCost()
                    + " modelMove=" + node.getModelMove()
                    + " movedEvent=" + node.getMovedEvent());
        }

        @Override
        public void edgeTraversed(Record from, Record to) {
            edges++;
            System.out.println("EDGE   #" + edges
                    + " " + from.getState() + " -> " + to.getState()
                    + " toCostSoFar=" + to.getCostSoFar()
                    + " toTotalCost=" + to.getTotalCost()
                    + " modelMove=" + to.getModelMove()
                    + " movedEvent=" + to.getMovedEvent());
        }

        @Override
        public void estimateComputed(Head head) {
            // DijkstraTail always estimates 0; nothing interesting to print here.
        }

        @Override
        public void initialNodeCreated(Record node) {
            System.out.println("INITIAL state=" + node.getState() + " costSoFar=" + node.getCostSoFar());
        }

        @Override
        public void finalNodeFound(Record node) {
            System.out.println("FINAL  state=" + node.getState()
                    + " costSoFar=" + node.getCostSoFar()
                    + " totalCost=" + node.getTotalCost());
        }

        @Override
        public void stoppedUnreliablyAt(Record rec) {
            System.out.println("UNRELIABLE STOP at state=" + (rec == null ? "null" : rec.getState()));
        }

        @Override
        public void close() {
        }
    }
}
