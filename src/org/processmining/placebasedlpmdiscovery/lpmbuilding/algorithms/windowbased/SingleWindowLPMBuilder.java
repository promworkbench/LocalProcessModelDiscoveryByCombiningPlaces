package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.windowbased;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.model.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;

public interface SingleWindowLPMBuilder {

    WindowLPMStorage build(IWindowInfo windowInfo);
}
