package org.processmining.placebasedlpmdiscovery.lpmbuilding.results;

import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

public class DefaultLPMBuildingResult implements LPMBuildingResult {

    private final GlobalLPMStorage storage;

    public DefaultLPMBuildingResult(GlobalLPMStorage storage) {
        this.storage = storage;
    }

    @Override
    public GlobalLPMStorage getStorage() {
        return this.storage;
    }
}
