package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.windowbased.singlewindowbuilders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.windowbased.SingleWindowLPMBuilder;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.lpmmodels.processtree.ProcessTree;

public class PTSingleWindowLPMBuilder implements SingleWindowLPMBuilder {
    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo) {
        return null;
    }

    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {

        ProcessTree pt = new ProcessTree(windowInfo.getWindow().get(windowInfo.getEndPos()));
        return null;
    }


}
