package org.processmining.placebasedlpmdiscovery.placediscovery.parameters;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.v7.postproc_after_tc.MyParameters;
//import org.processmining.v8.eSTMinerGIT.Parameters;

public class EstMinerPlaceDiscoveryParameters extends PlaceDiscoveryParameters {
    private MyParameters wrappedParameters;

    public EstMinerPlaceDiscoveryParameters() {
        this.wrappedParameters = new MyParameters();
        this.wrappedParameters.setClassifier(new XEventNameClassifier());
//        this.wrappedParameters.setRemoveImps(false);
        this.wrappedParameters.setTimeAllowance(36000000);
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
