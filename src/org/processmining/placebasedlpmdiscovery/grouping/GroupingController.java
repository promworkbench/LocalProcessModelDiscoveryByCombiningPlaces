package org.processmining.placebasedlpmdiscovery.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMGroupingFinished;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.RunLPMGroupingEmittableData;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupingProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupingController implements DataListener {

    private final DataCommunicationController dc;
    private final ModelDistanceController modelDistanceController;

    @Inject
    public GroupingController(DataCommunicationController dc, ModelDistanceController modelDistanceController) {
        this.dc = dc;
//        dc.registerDataListener(this, EmittableDataType.RunLPMGrouping);
        this.modelDistanceController = modelDistanceController;
    }

    public void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

        int[] membership = ClusteringLPMs.cluster(
                lpmList,
                modelDistanceController.getDistanceMatrix(lpmList),
                config.getClusteringConfig().getClusteringAlgorithm(),
                config.getClusteringConfig().getClusteringParam());

        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty(config.getIdentifier(), membership[i]);
        }

        this.dc.emit(new LPMGroupingFinished(config.getIdentifier()));
    }

    @Override
    public void receive(EmittableData data) {
        if (data.getType().equals(EmittableDataType.RunLPMGrouping)) {
            RunLPMGroupingEmittableData cData = (RunLPMGroupingEmittableData) data;
            GroupingConfig config = new DefaultGroupingConfig(
                    cData.getIdentifier(),
                    new DefaultClusteringConfig(cData.getClusteringAlgorithm(), cData.getClusteringParameters()),
                    cData.getModelDistanceConfig()
            );
            this.groupLPMs(cData.getLPMs(), config);
        }
    }
}
