package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooserParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;

import java.util.Objects;

public class PlaceBasedLPMDiscoveryParameters {

    // log
    private final XLog log;

    // place discovery parameters
    private PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId;
    private boolean useDefaultPlaceDiscoveryParameters;
    private PlaceDiscoveryParameters placeDiscoveryParameters;

    // place chooser parameters
    private final PlaceChooserParameters placeChooserParameters;

    // local process model discovery parameters
    private final LPMCombinationParameters lpmCombinationParameters;

    // local process model filter parameters
    private final LPMFilterParameters lpmFilterParameters;
    private int lpmCount;

    // limit
    private long timeLimit;

    public PlaceBasedLPMDiscoveryParameters(XLog log) {
        this.log = log;
        this.placeDiscoveryAlgorithmId = PlaceDiscoveryAlgorithmId.ESTMiner;
        this.placeDiscoveryParameters = new EstMinerPlaceDiscoveryParameters();
        this.placeChooserParameters = new PlaceChooserParameters(log);
        this.lpmCombinationParameters = new LPMCombinationParameters();
        this.lpmFilterParameters = new LPMFilterParameters();
        this.lpmCount = 100;
        this.timeLimit = 600000;
    }

    public XLog getLog() {
        return log;
    }

    public PlaceDiscoveryAlgorithmId getPlaceDiscoveryAlgorithmId() {
        return placeDiscoveryAlgorithmId;
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

    public LPMFilterParameters getLpmFilterParameters() {
        return lpmFilterParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceBasedLPMDiscoveryParameters that = (PlaceBasedLPMDiscoveryParameters) o;
        return log.equals(that.log) &&
                placeDiscoveryAlgorithmId == that.placeDiscoveryAlgorithmId &&
                placeDiscoveryParameters.equals(that.placeDiscoveryParameters) &&
                lpmCombinationParameters.equals(that.lpmCombinationParameters) &&
                lpmFilterParameters.equals(that.lpmFilterParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(log, placeDiscoveryAlgorithmId, placeDiscoveryParameters,
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
}
