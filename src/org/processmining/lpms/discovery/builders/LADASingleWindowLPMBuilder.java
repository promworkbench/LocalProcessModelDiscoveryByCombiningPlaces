package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;

public class LADASingleWindowLPMBuilder implements SingleWindowLPMBuilder {

    @Override
    public WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {
//        Activity newAct = windowInfo.getWindow().get(windowInfo.getWindow().size()-1);
//        prevWindowResult.extend(newAct);
        return null;
    }
}
