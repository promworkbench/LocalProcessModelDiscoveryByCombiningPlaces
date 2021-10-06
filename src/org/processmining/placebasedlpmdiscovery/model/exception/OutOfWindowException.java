package org.processmining.placebasedlpmdiscovery.model.exception;

public class OutOfWindowException extends RuntimeException {
    private static final String MESSAGE = "Trying to access index %d but the window size is %d. " +
            "You have requested link between %d and %d which have difference of %d " +
            "but the max allowed difference by the window is %d.";
    private final int windowSize;
    private final int index;
    private final int parentPos;
    private final int childPos;

    public OutOfWindowException(int windowSize, int index, int parentPos, int childPos) {
        super(String.format(MESSAGE, index, windowSize, parentPos, childPos, Math.abs(parentPos - childPos),
                windowSize));
        this.windowSize = windowSize;
        this.index = index;
        this.parentPos = parentPos;
        this.childPos = childPos;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getIndex() {
        return index;
    }

    public int getParentPos() {
        return parentPos;
    }

    public int getChildPos() {
        return childPos;
    }
}
