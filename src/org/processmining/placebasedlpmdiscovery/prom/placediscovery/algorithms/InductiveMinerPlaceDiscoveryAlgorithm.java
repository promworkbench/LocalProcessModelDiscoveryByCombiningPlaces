package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetFactory;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;
import org.processmining.plugins.InductiveMiner.plugins.IM;


public class InductiveMinerPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<InductiveMinerPlaceDiscoveryParameters, AcceptingPetriNet> {

    public InductiveMinerPlaceDiscoveryAlgorithm(PetriNetPlaceConverter converter, InductiveMinerPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        Object[] res = IM.minePetriNet(ContextKeeper.getContext(), log, this.parameters.getMiningParameters());
        AcceptingPetriNet petriNet = AcceptingPetriNetFactory.createAcceptingPetriNet((Petrinet) res[0], (Marking) res[1], (Marking) res[2]);
        return new StandardPlaceDiscoveryResult(converter.convert(petriNet));
    }
}
