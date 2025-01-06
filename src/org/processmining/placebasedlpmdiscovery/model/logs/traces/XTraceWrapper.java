package org.processmining.placebasedlpmdiscovery.model.logs.traces;

import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.model.logs.XEventWrapper;
import org.processmining.placebasedlpmdiscovery.utils.Constants;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class XTraceWrapper implements EventLogTrace<XEventWrapper> {

    private final List<XEventWrapper> events;

    public XTraceWrapper(XTrace trace) {
        this.events = Collections.unmodifiableList(trace.stream().map(xEvent -> new XEventWrapper(xEvent,
                Constants.Logs.DEFAULT_CASE_ATTRIBUTE)).collect(Collectors.toList()));
    }

    @Override
    public void forEach(Consumer<XEventWrapper> action) {
        events.forEach(action);
    }
}
