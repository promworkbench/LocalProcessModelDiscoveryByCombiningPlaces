package org.processmining.placebasedlpmdiscovery.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetFactory;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.plugins.IM;


public class InductiveMinerPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<InductiveMinerPlaceDiscoveryParameters, AcceptingPetriNet> {

    public InductiveMinerPlaceDiscoveryAlgorithm(PetriNetPlaceConverter converter, InductiveMinerPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        PlaceDiscoveryResult result = new PlaceDiscoveryResult();
        Object[] res = IM.minePetriNet(Main.getContext(), log, this.parameters.getMiningParameters());
        AcceptingPetriNet petriNet = AcceptingPetriNetFactory.createAcceptingPetriNet((Petrinet) res[0], (Marking) res[1], (Marking) res[2]);
        result.setPlaces(converter.convert(petriNet));
        return result;
    }
}
