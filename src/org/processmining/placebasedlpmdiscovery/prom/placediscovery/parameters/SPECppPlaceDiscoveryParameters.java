package org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.specpp.config.ConfigFactory;
import org.processmining.specpp.config.DataExtractionParameters;
import org.processmining.specpp.config.PreProcessingParameters;
import org.processmining.specpp.config.SPECppConfigBundle;
import org.processmining.specpp.config.presets.BaseParameters;
import org.processmining.specpp.config.presets.PlaceOracleComponentConfig;
import org.processmining.specpp.config.presets.PlaceOracleParameters;

public class SPECppPlaceDiscoveryParameters extends PlaceDiscoveryParameters {
    private final SPECppConfigBundle configBundle;

    public SPECppPlaceDiscoveryParameters() {
        this.configBundle = ConfigFactory.create(PreProcessingParameters.getDefault(),
                DataExtractionParameters.getDefault(), new PlaceOracleComponentConfig(),
                new BaseParameters(), new PlaceOracleParameters());
    }

    public SPECppConfigBundle getConfigBundle() {
        return configBundle;
    }

    @Override
    public PlaceDiscoveryAlgorithm<SPECppPlaceDiscoveryParameters, AcceptingPetriNet> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }
}
