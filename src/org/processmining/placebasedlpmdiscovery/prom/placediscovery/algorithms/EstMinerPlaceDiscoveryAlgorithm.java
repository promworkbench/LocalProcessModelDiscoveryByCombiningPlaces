package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;
import org.processmining.v7.postproc_after_tc.MyFirstMinerPlugin;
//import org.processmining.v8.eSTMinerGIT.MainPlugIn;

public class EstMinerPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<
        EstMinerPlaceDiscoveryParameters,
        AcceptingPetriNet> {

    public EstMinerPlaceDiscoveryAlgorithm(PetriNetPlaceConverter converter, EstMinerPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        StandardPlaceDiscoveryResult result = new StandardPlaceDiscoveryResult();
        try {
            MyFirstMinerPlugin mainPlugIn = new MyFirstMinerPlugin();
            Object[] modelAndLog = mainPlugIn.discover(ContextKeeper.getContext(), log, parameters.getWrappedParameters());
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

