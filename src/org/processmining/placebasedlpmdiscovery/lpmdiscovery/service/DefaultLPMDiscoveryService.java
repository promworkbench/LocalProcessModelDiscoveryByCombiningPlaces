package org.processmining.placebasedlpmdiscovery.lpmdiscovery.service;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public class DefaultLPMDiscoveryService implements LPMDiscoveryService {

    private final DataCommunicationController dc;

    @Inject
    public DefaultLPMDiscoveryService(DataCommunicationController dc) {
        this.dc = dc;
    }

    @Override
    public LPMDiscoveryResult runLPMDiscovery(EventLog log, PlaceSet placeSet) {
        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
        LPMDiscoveryResult result = Main.createDefaultBuilder(log.getOriginalLog(), parameters).build()
                .run(new StandardLPMDiscoveryInput(log, new FPGrowthForPlacesLPMBuildingInput(log,
                                placeSet.getPlaces().getPlaces())), parameters);
        dc.emit(new LPMSetDiscoveredEmittableData(result));
        return result;
    }
}
