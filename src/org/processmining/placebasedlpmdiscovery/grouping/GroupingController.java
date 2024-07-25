package org.processmining.placebasedlpmdiscovery.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMGroupingFinished;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.RunLPMGroupingEmittableData;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public class GroupingController implements DataListener {

    private final DataCommunicationController dc;
    private final GroupingService groupingService;

    @Inject
    public GroupingController(DataCommunicationController dc, GroupingService groupingService) {
        this.dc = dc;
        dc.registerDataListener(this, EmittableDataType.RunLPMGrouping);
        this.groupingService = groupingService;
    }

    public void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config) {
        this.groupingService.groupLPMs(lpms, config);
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
