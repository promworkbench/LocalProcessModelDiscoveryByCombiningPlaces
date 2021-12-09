package org.processmining.placebasedlpmdiscovery.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.v7.postproc_after_tc.MyFirstMinerPlugin;

public class EstMinerPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<
        EstMinerPlaceDiscoveryParameters,
        AcceptingPetriNet> {

    public EstMinerPlaceDiscoveryAlgorithm(PetriNetPlaceConverter converter, EstMinerPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        PlaceDiscoveryResult result = new PlaceDiscoveryResult();
        try {
            MyFirstMinerPlugin mainPlugIn = new MyFirstMinerPlugin();
            Object[] modelAndLog = mainPlugIn.discoverWithImplicit(Main.getContext(), log, parameters.getWrappedParameters());
            AcceptingPetriNet acceptingPetriNet = new AcceptingPetriNetImpl(
                    (Petrinet) modelAndLog[0], (Marking) modelAndLog[1], (Marking) modelAndLog[2]);
            result.setPlaces(this.converter.convert(acceptingPetriNet));
            result.setLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

