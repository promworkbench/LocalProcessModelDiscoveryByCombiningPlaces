package org.processmining.placebasedlpmdiscovery.lpmbuilding;

import org.deckfour.xes.model.XLog;
import org.processmining.lpms.discovery.DiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.LPMStorage;

public interface LPMBuilding {

    static LPMBuilding getInstance() {
        return windowBased(DiscoveryParameters.Default.proximity);
    }

    static LPMBuilding windowBased(int proximity) {
        return treeBased(proximity);
    }

    static LPMBuilding treeBased(int proximity) {
        return new PTBasedLPMBuilding(proximity);
    }

    LPMStorage from(XLog log);
}
