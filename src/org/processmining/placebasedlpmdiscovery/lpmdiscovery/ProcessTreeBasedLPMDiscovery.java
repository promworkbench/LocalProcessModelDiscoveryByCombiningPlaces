package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.lpms.discovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.LPMBuilding;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.LPMStorage;

public class ProcessTreeBasedLPMDiscovery implements LPMDiscovery {
    @Override
    public LPMDiscoveryResult from(XLog log, int proximity) {
        LPMStorage lpmStorage = LPMBuilding.treeBased(proximity).from(log);

        throw new NotImplementedException("hihihi");
    }
}
