package org.processmining.placebasedlpmdiscovery.lpmdiscovery.service;

import com.google.inject.Inject;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class DefaultLPMDiscoveryService implements LPMDiscoveryService {

    private final DataCommunicationController dc;

    @Inject
    public DefaultLPMDiscoveryService(DataCommunicationController dc) {
        this.dc = dc;
    }

    @Override
    public LPMDiscoveryResult runLPMDiscovery(EventLog log, PlaceSet placeSet) {
        LPMDiscoveryResult result = Main.createDefaultBuilder(log.getOriginalLog(), placeSet,
                new PlaceBasedLPMDiscoveryParameters(log)).build().run(
                        new StandardLPMDiscoveryInput(log, new FPGrowthForPlacesLPMBuildingInput(log,
                                placeSet.getPlaces().getPlaces())));
        dc.emit(new LPMSetDiscoveredEmittableData(result));
        return result;
    }
}
