package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.lpmmodels.processtree.ProcessTree;

public class PTLADAWindowLPMBuilder implements LADAWindowLPMBuilder {

    @Override
    public WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {

        ProcessTree pt = new ProcessTree(windowInfo.getWindow().get(windowInfo.getEndPos()));
        return null;
    }
}
