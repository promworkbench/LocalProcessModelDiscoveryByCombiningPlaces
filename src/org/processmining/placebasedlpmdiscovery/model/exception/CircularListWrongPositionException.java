package org.processmining.placebasedlpmdiscovery.model.exception;

public class CircularListWrongPositionException extends RuntimeException {
    private static final long serialVersionUID = -6082529853344736507L;
    private static final String MESSAGE = "The requested position is %d and the actual position is %d. " +
            "The difference can be between 0 and %d and the actual difference is %d.";
    private final int requestedPosition;
    private final int actualPosition;
    private final int size;

    public CircularListWrongPositionException(int requestedPosition, int actualPosition, int size) {
        super(String.format(MESSAGE, requestedPosition, actualPosition, size, requestedPosition - actualPosition));
        this.requestedPosition = requestedPosition;
        this.actualPosition = actualPosition;
        this.size = size;
    }

    public int getRequestedPosition() {
        return requestedPosition;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public int getSize() {
        return size;
    }
}
