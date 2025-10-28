package org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.v7.postproc_after_tc.MyParameters;
//import org.processmining.v8.eSTMinerGIT.Parameters;

public class EstMinerPlaceDiscoveryParameters implements PlaceDiscoveryParameters {
    private MyParameters wrappedParameters;

    public EstMinerPlaceDiscoveryParameters() {
        this.wrappedParameters = new MyParameters();
        this.wrappedParameters.setClassifier(new XEventNameClassifier());
        this.wrappedParameters.setThreshold(0.5);
        this.wrappedParameters.setTimeAllowance(3600000);
    }

    @Override
    public PlaceDiscoveryAlgorithm<EstMinerPlaceDiscoveryParameters, AcceptingPetriNet> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }

    public MyParameters getWrappedParameters() {
        return wrappedParameters;
    }

    public void setWrappedParameters(MyParameters wrappedParameters) {
        this.wrappedParameters = wrappedParameters;
    }
}
