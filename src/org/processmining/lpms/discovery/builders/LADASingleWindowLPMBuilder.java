package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;

public class LADASingleWindowLPMBuilder implements SingleWindowLPMBuilder {

    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo) {
//        WindowLPMStorage windowStorage = WindowLPMStorage.getEmpty();
//        for (int i = 1; i < windowInfo.getWindow().size(); ++i) {
//            windowStorage = build(IWindowInfo.subWindow(windowInfo, 0, i), windowStorage);
//        }
        return null;
    }

    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {
        return null;
    }
}
