package org.processmining.placebasedlpmdiscovery.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetFactory;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.StandardPlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.plugins.heuristicsnet.miner.heuristics.converter.HeuristicsNetToPetriNetConverter;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMinerPlugin;

public class HeuristicMinerPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<HeuristicMinerPlaceDiscoveryParameters, AcceptingPetriNet> {

    public HeuristicMinerPlaceDiscoveryAlgorithm(PetriNetPlaceConverter converter, HeuristicMinerPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        HeuristicsNet net = FlexibleHeuristicsMinerPlugin.run(Main.getContext(), log, parameters.getSettings());
        Object[] res = HeuristicsNetToPetriNetConverter.converter(Main.getContext(), net);
        AcceptingPetriNet petriNet = AcceptingPetriNetFactory.createAcceptingPetriNet((Petrinet) res[0], (Marking) res[1], new Marking());
        return new StandardPlaceDiscoveryResult(this.converter.convert(petriNet));
    }
}
