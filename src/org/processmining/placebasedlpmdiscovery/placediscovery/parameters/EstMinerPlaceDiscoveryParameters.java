package org.processmining.placebasedlpmdiscovery.placediscovery.parameters;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.v8.eSTMinerGIT.Parameters;

public class EstMinerPlaceDiscoveryParameters extends PlaceDiscoveryParameters {
    private Parameters wrappedParameters;

    public EstMinerPlaceDiscoveryParameters() {
        this.wrappedParameters = new Parameters();
        this.wrappedParameters.setClassifier(new XEventNameClassifier());
        this.wrappedParameters.setThreshold(0.5);
        this.wrappedParameters.setTimeAllowance(3600000);
    }

    @Override
    public PlaceDiscoveryAlgorithm<EstMinerPlaceDiscoveryParameters, AcceptingPetriNet> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }

    public Parameters getWrappedParameters() {
        return wrappedParameters;
    }

    public void setWrappedParameters(Parameters wrappedParameters) {
        this.wrappedParameters = wrappedParameters;
    }
}
