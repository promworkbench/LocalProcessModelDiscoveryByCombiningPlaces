package org.processmining.placebasedlpmdiscovery.model.interruptible;

public abstract class Interruptible implements CanBeInterrupted {

    protected boolean stop;

    @Override
    public void interrupt() {
        this.stop = true;
    }
}
