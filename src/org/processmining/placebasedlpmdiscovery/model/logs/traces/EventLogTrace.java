package org.processmining.placebasedlpmdiscovery.model.logs.traces;

import org.processmining.placebasedlpmdiscovery.model.logs.Event;

import java.util.function.Consumer;

public interface EventLogTrace<EVENT extends Event> {

    void forEach(Consumer<EVENT> action);
}
