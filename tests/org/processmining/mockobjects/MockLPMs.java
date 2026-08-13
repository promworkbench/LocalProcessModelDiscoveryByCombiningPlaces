package org.processmining.mockobjects;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class MockLPMs {

    public static LocalProcessModel getSequenceLPM_abc() {
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(MockPlaces.getSequencePlace_ab());
        lpm.addPlace(MockPlaces.getSequencePlace_bc());
        return lpm;
    }

    // a, then a choice between b and c (both consume the single token "a" produces)
    public static LocalProcessModel getChoiceLPM_aXbc() {
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(MockPlaces.getChoiceOutPlace());
        return lpm;
    }

    // a, then b and c independently and concurrently (each has its own place fed by "a", with no
    // place ordering b relative to c)
    public static LocalProcessModel getConcurrentLPM_aANDbc() {
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(MockPlaces.getSequencePlace_ab());
        lpm.addPlace(MockPlaces.getSequencePlace_ac());
        return lpm;
    }
}
