package src.org.processmining.mockobjects;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class MockLPMs {

    public static LocalProcessModel getSequenceLPM_abc() {
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(MockPlaces.getSequencePlace_ab());
        lpm.addPlace(MockPlaces.getSequencePlace_bc());
        return lpm;
    }
}
