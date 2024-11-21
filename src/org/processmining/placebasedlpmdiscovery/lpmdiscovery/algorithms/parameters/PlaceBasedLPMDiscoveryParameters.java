package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlgType;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.FPGrowthForPlacesLPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.model.logs.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooserParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.PlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlaceBasedLPMDiscoveryParameters implements LPMDiscoveryParameters {

    // place discovery parameters
    private PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId;
    private boolean useDefaultPlaceDiscoveryParameters;
    private PlaceDiscoveryParameters placeDiscoveryParameters;

    // place chooser parameters
    private final PlaceChooserParameters placeChooserParameters;

    // local process model discovery parameters
    private LPMCombinationParameters lpmCombinationParameters;
    private LPMBuildingParameters lpmBuildingParameters;
    private LPMBuildingAlgType lpmBuildingAlgType;

    // local process model filter parameters
    private LPMFilterParameters lpmFilterParameters;
    private int lpmCount;

    // limit
    private long timeLimit;

    // event attribute summary
    private final Map<String, AttributeSummary<?, ?>> eventAttributeSummary;

    public PlaceBasedLPMDiscoveryParameters(EventLog log) {
        this.placeDiscoveryAlgorithmId = PlaceDiscoveryAlgorithmId.ESTMiner;
        this.placeDiscoveryParameters = new EstMinerPlaceDiscoveryParameters();
        this.placeChooserParameters =
                new PlaceChooserParameters(log.getActivities().stream().map(Activity::getName).collect(Collectors.toSet()));
        this.lpmCombinationParameters = new LPMCombinationParameters();
        this.lpmBuildingAlgType = LPMBuildingAlgType.FPGrowthForPlaces;
        this.lpmBuildingParameters = new FPGrowthForPlacesLPMBuildingParameters(this.lpmCombinationParameters,
                this.placeChooserParameters);
        this.lpmFilterParameters = new LPMFilterParameters();
        this.lpmCount = 100;
        this.timeLimit = 600000;
        this.eventAttributeSummary = new HashMap<>();
    }

    public PlaceDiscoveryAlgorithmId getPlaceDiscoveryAlgorithmId() {
        return placeDiscoveryAlgorithmId;
    }

    public void setLpmCombinationParameters(LPMCombinationParameters lpmCombinationParameters) {
        this.lpmCombinationParameters = lpmCombinationParameters;
    }

    public LPMBuildingParameters getLpmBuildingParameters() {
        return lpmBuildingParameters;
    }

    public void setLpmBuildingParameters(LPMBuildingParameters lpmBuildingParameters) {
        this.lpmBuildingParameters = lpmBuildingParameters;
    }

    public void setLpmFilterParameters(LPMFilterParameters lpmFilterParameters) {
        this.lpmFilterParameters = lpmFilterParameters;
    }

    public void setPlaceDiscoveryAlgorithmId(PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId) {
        this.placeDiscoveryAlgorithmId = placeDiscoveryAlgorithmId;
    }

    public PlaceDiscoveryParameters getPlaceDiscoveryParameters() {
        return placeDiscoveryParameters;
    }

    public void setPlaceDiscoveryParameters(PlaceDiscoveryParameters placeDiscoveryParameters) {
        this.placeDiscoveryParameters = placeDiscoveryParameters;
    }

    public boolean isUseDefaultPlaceDiscoveryParameters() {
        return useDefaultPlaceDiscoveryParameters;
    }

    public void setUseDefaultPlaceDiscoveryParameters(boolean useDefaultPlaceDiscoveryParameters) {
        this.useDefaultPlaceDiscoveryParameters = useDefaultPlaceDiscoveryParameters;
    }

    public LPMCombinationParameters getLpmCombinationParameters() {
        return lpmCombinationParameters;
    }

    public LPMBuildingAlgType getLpmBuildingAlgType() {
        return lpmBuildingAlgType;
    }

    public void setLpmBuildingAlgType(LPMBuildingAlgType lpmBuildingAlgType) {
        this.lpmBuildingAlgType = lpmBuildingAlgType;
    }

    public LPMFilterParameters getLpmFilterParameters() {
        return lpmFilterParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceBasedLPMDiscoveryParameters that = (PlaceBasedLPMDiscoveryParameters) o;
        return placeDiscoveryAlgorithmId == that.placeDiscoveryAlgorithmId &&
                placeDiscoveryParameters.equals(that.placeDiscoveryParameters) &&
                lpmCombinationParameters.equals(that.lpmCombinationParameters) &&
                lpmFilterParameters.equals(that.lpmFilterParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeDiscoveryAlgorithmId, placeDiscoveryParameters,
                lpmCombinationParameters, lpmFilterParameters);
    }

    public void setDefaultPlaceDiscoveryParameters() {
        if (this.placeDiscoveryAlgorithmId == PlaceDiscoveryAlgorithmId.ESTMiner)
            this.placeDiscoveryParameters = new EstMinerPlaceDiscoveryParameters();
        else if (this.placeDiscoveryAlgorithmId == PlaceDiscoveryAlgorithmId.InductiveMiner)
            this.placeDiscoveryParameters = new InductiveMinerPlaceDiscoveryParameters();
        else if (this.placeDiscoveryAlgorithmId == PlaceDiscoveryAlgorithmId.HeuristicMiner)
            this.placeDiscoveryParameters = new HeuristicMinerPlaceDiscoveryParameters();
    }

    public int getLpmCount() {
        return lpmCount;
    }

    public void setLpmCount(int lpmCount) {
        this.lpmCount = lpmCount;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public PlaceChooserParameters getPlaceChooserParameters() {
        return placeChooserParameters;
    }

    public Map<String, AttributeSummary<?, ?>> getEventAttributeSummary() {
        return eventAttributeSummary;
    }

    @Override
    public String toString() {
        return "PlaceBasedLPMDiscoveryParameters{" +
                "placeDiscoveryAlgorithmId=" + placeDiscoveryAlgorithmId +
                ", useDefaultPlaceDiscoveryParameters=" + useDefaultPlaceDiscoveryParameters +
                ", placeChooserParameters=" + placeChooserParameters +
                ", lpmCombinationParameters=" + lpmCombinationParameters +
                ", lpmFilterParameters=" + lpmFilterParameters +
                ", lpmCount=" + lpmCount +
                ", timeLimit=" + timeLimit +
                '}';
    }

    public LPMBuildingParameters getLPMBuildingParameters() {
        return this.lpmBuildingParameters;
    }
}
