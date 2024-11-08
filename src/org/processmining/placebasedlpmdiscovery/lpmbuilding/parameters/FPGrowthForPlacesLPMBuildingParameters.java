package org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooserParameters;

public class FPGrowthForPlacesLPMBuildingParameters implements LPMBuildingParameters {
    private final LPMCombinationParameters lpmCombinationParameters;
    private final PlaceChooserParameters placeChooserParameters;

    public FPGrowthForPlacesLPMBuildingParameters(LPMCombinationParameters lpmCombinationParameters, PlaceChooserParameters placeChooserParameters) {

        this.lpmCombinationParameters = lpmCombinationParameters;
        this.placeChooserParameters = placeChooserParameters;
    }

    public LPMCombinationParameters getLPMCombinationParameters() {
        return this.lpmCombinationParameters;
    }

    public PlaceChooserParameters getPlaceChooserParameters() {
        return this.placeChooserParameters;
    }
}
