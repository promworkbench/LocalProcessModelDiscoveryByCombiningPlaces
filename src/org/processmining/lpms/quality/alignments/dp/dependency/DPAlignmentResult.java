package org.processmining.lpms.quality.alignments.dp.dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DPAlignmentResult {

    public static int SYNC_MOVE = 0;
    public static int LOG_MOVE = 1;

    private final List<Integer[]> alignments;
    private final int length;
    private int cursor;

    public DPAlignmentResult(int length) {
        this(length, 0);
    }

    public DPAlignmentResult(int length, int cursor) {
        this(length, cursor, true);
    }

    private DPAlignmentResult(List<Integer[]> alignments, int length, int cursor) {
        this.alignments = alignments;
        this.length = length;
        this.cursor = cursor;
    }

    public DPAlignmentResult(int length, boolean empty) {
        this(length, 0, empty);
    }

    public DPAlignmentResult(int length, int cursor, boolean empty) {
        this.alignments = new ArrayList<>();
        this.length = length;
        this.cursor = cursor;
        if (!empty) {
            this.alignments.add(new Integer[length]);
        }
    }

    public static DPAlignmentResult crossProduct(DPAlignmentResult left, DPAlignmentResult right,
                                                 Set<DPAlignmentResult> mustShare) {
        if (left == null || right == null) {
            return null;
        }

        DPAlignmentResult crossProduct = new DPAlignmentResult(left.length, left.cursor);
        for (Integer[] leftA : left.alignments) {
            for (Integer[] rightA : right.alignments) {
                boolean shareAll = true;
                for (DPAlignmentResult mustShareA : mustShare) {
                    if (!shareAnySubAlignment(leftA, rightA, mustShareA)) {
                        shareAll = false;
                        break;
                    }
                }
                if (!shareAll) {
                    continue;
                }

                Integer[] combined = new Integer[left.length];
                for (int i = 0; i < left.alignmentLength(); ++i) {
                    combined[i] = leftA[i] * rightA[i];
                }
                crossProduct.add(combined);
            }
        }
        return crossProduct;
    }

    private static boolean shareAnySubAlignment(Integer[] leftA, Integer[] rightA, DPAlignmentResult mustShareA) {
        for (Integer[] shared : mustShareA.getAlignments()) {
            boolean holds = true;
            for (int i = 0; i < shared.length; ++i) {
                if (shared[i] == SYNC_MOVE && !leftA[i].equals(rightA[i])) {
                    holds = false;
                    break;
                }
            }
            if (holds) {
                return true;
            }
        }
        return false;
    }

    public static DPAlignmentResult createAllLogMoves(int length) {
        DPAlignmentResult result = new DPAlignmentResult(length, false);
        for (int i = 0; i < length; ++i) {
            result.insert(LOG_MOVE);
        }
        return result;
    }

    public static DPAlignmentResult createEmpty(int length) {
        return new DPAlignmentResult(length, length, true);
    }

    public List<Integer[]> getAlignments() {
        return alignments;
    }

    public void add(Integer[] alignment) {
        if (alignment.length != this.length) {
            throw new IllegalArgumentException("Alignment length must be " + this.length);
        }
        this.alignments.add(alignment);
    }

    public void insert(int move) {
        if (move != SYNC_MOVE && move != LOG_MOVE) {
            throw new IllegalArgumentException("Move must be either " + SYNC_MOVE + " (SYNC_MOVE) or " + LOG_MOVE +
                    " (LOG_MOVE)");
        }
        if (cursor >= length) {
            throw new IllegalStateException("Alignment result already holds its fixed capacity of " + length + " " +
                    "moves");
        }
        for (Integer[] alignment : this.alignments) {
            alignment[cursor] = move;
        }
        ++cursor;
    }

    public DPAlignmentResult copyOneBigger() {
        List<Integer[]> copiedAlignments = new ArrayList<>(this.alignments.size());
        for (Integer[] alignment : this.alignments) {
            copiedAlignments.add(Arrays.copyOf(alignment, alignment.length + 1));
        }
        return new DPAlignmentResult(copiedAlignments, this.length + 1, this.cursor);
    }

    public int alignmentLength() {
        return this.cursor;
    }

    public void addAll(DPAlignmentResult from) {
        for (Integer[] alignment : from.getAlignments()) {
            this.add(alignment);
        }
    }

    public DPAlignmentResult copy() {
        List<Integer[]> copiedAlignments = new ArrayList<>(this.alignments.size());
        for (Integer[] alignment : this.alignments) {
            copiedAlignments.add(Arrays.copyOf(alignment, alignment.length));
        }
        return new DPAlignmentResult(copiedAlignments, this.length, this.cursor);
    }

    public boolean isEmpty() {
        return this.alignments.isEmpty();
    }

    public void createNew() {
        this.alignments.add(new Integer[length]);
    }
}