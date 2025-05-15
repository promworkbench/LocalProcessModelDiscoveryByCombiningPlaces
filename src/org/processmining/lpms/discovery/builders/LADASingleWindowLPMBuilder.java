package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;

public class LADASingleWindowLPMBuilder implements SingleWindowLPMBuilder {

    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo) {
        WindowLPMStorage windowStorage = WindowLPMStorage.lada();
        for (int i = 1; i < windowInfo.getWindow().size(); ++i) {
            windowStorage = build(windowInfo.subWindow(0, i), windowStorage);
        }
        return windowStorage;
    }

    @Override
    public WindowLPMStorage build(IWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {
//        Activity newAct = windowInfo.getWindow().get(windowInfo.getWindow().size()-1);
//        prevWindowResult.extend(newAct);
        return null;
    }
}
