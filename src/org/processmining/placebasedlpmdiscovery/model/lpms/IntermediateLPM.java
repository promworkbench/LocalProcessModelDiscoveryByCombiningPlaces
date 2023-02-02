package org.processmining.placebasedlpmdiscovery.model.lpms;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.Passage;
import org.processmining.placebasedlpmdiscovery.model.logs.Event;

import java.util.List;
import java.util.Set;

public class IntermediateLPM {

    private List<Event> replayedEvents;
    private Set<Passage> usedPassages;
    private Set<Transition> usedTransitions;
    private LocalProcessModel lpm;

    public LocalProcessModel getLpm() {
        return lpm;
    }

    public void setLpm(LocalProcessModel lpm) {
        this.lpm = lpm;
    }
}
