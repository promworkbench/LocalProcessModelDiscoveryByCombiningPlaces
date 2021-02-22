package org.processmining.localprocessmodeldiscoverybycombiningplaces.model;

import java.util.ArrayList;
import java.util.List;

public class InterrupterSubject {

    private final List<CanBeInterrupted> observers;

    public InterrupterSubject() {
        observers = new ArrayList<>();
    }

    public void addObserver(CanBeInterrupted observer) {
        observers.add(observer);
    }

    public void notifyInterruption() {
        for (CanBeInterrupted observer : observers)
            observer.interrupt();
    }
}
