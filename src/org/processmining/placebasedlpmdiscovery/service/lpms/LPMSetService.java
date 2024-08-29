package org.processmining.placebasedlpmdiscovery.service.lpms;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public interface LPMSetService {

    LPMDiscoveryResult getOriginalLPMSet();

    LPMDiscoveryResult getLPMSet();
}
