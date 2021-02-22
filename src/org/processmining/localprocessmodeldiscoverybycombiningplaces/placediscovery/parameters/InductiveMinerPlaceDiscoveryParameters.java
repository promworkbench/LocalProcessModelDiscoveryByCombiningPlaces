package org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.plugins.dialogs.IMMiningDialog;

public class InductiveMinerPlaceDiscoveryParameters extends PlaceDiscoveryParameters {
    private MiningParameters miningParameters;
    private IMMiningDialog.Variant variant;

    public InductiveMinerPlaceDiscoveryParameters() {
        this.miningParameters = new MiningParametersIM();
    }

    @Override
    public PlaceDiscoveryAlgorithm<InductiveMinerPlaceDiscoveryParameters, AcceptingPetriNet> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }

    public MiningParameters getMiningParameters() {
        return miningParameters;
    }

    public void setMiningParameters(MiningParameters miningParameters) {
        this.miningParameters = miningParameters;
    }

    public IMMiningDialog.Variant getVariant() {
        return variant;
    }

    public void setVariant(IMMiningDialog.Variant variant) {
        this.variant = variant;
    }
}
