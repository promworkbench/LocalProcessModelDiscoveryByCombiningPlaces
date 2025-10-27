package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;

public interface PlacesProviderFactory {

    PlacesProvider create(XLog log);
}
