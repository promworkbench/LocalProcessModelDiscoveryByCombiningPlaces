package org.processmining.placebasedlpmdiscovery.placediscovery;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;

public class PetriNetPlaceDiscovery implements PlaceDiscovery {

    private final Petrinet petriNet;

    public PetriNetPlaceDiscovery(Petrinet petriNet) {
        this.petriNet = petriNet;
    }

    @Override
    public PlaceDiscoveryResult getPlaces() {
        AcceptingPetriNet acceptingPetriNet = new AcceptingPetriNetImpl(this.petriNet);
        PetriNetPlaceConverter converter = new PetriNetPlaceConverter();
        PlaceDiscoveryResult result = new PlaceDiscoveryResult();
        result.setPlaces(converter.convert(acceptingPetriNet));
        return result;
    }
}
