package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.windowbased;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.WindowBasedLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.WindowBasedLPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowToGlobalLPMStorageTransporter;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.traversals.EventLogWindowTraversal;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

public class WindowBasedLPMBuildingAlg implements LPMBuildingAlg {

    private final SingleWindowLPMBuilder singleWindowLPMBuilder;

    public WindowBasedLPMBuildingAlg(SingleWindowLPMBuilder singleWindowLPMBuilder) {
        this.singleWindowLPMBuilder = singleWindowLPMBuilder;
    }

    @Override
    public LPMBuildingResult build(LPMBuildingInput input, LPMBuildingParameters parameters) {
        // cast input and parameters
        if (!(parameters instanceof WindowBasedLPMBuildingParameters)) {
            throw new IllegalArgumentException("The FPGrowthForPlacesLPMBuildingAlg does not work for " +
                    "parameters of type " + parameters.getClass());
        }
        WindowBasedLPMBuildingParameters cParameters = (WindowBasedLPMBuildingParameters) parameters;

        if (!(input instanceof WindowBasedLPMBuildingInput)) {
            throw new IllegalArgumentException("The FPGrowthForPlacesLPMBuildingAlg does not work for " +
                    "input of type " + input.getClass());
        }
        WindowBasedLPMBuildingInput cInput = (WindowBasedLPMBuildingInput) input;
        return build(cInput, cParameters);
    }

    private LPMBuildingResult build(WindowBasedLPMBuildingInput input, WindowBasedLPMBuildingParameters parameters) {
        GlobalLPMStorage lpmStorage = GlobalLPMStorage.getInstance();
        WindowToGlobalLPMStorageTransporter storageTransporter = WindowToGlobalLPMStorageTransporter.getInstance();

        // traverse event log and build lpms
        EventLogWindowTraversal traversal = EventLogWindowTraversal
                .getInstance(input.getEventLog(), parameters.getWindowSize());
        WindowLPMStorage windowStorage = null;
        while (traversal.hasNext()) {
            IWindowInfo windowInfo = traversal.next();
            windowStorage = singleWindowLPMBuilder.build(windowInfo, windowStorage);
            storageTransporter.move(windowStorage, lpmStorage);
        }

        return null;
    }
}
