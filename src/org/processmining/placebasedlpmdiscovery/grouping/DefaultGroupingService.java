package org.processmining.placebasedlpmdiscovery.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceService;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultGroupingService implements GroupingService {

    private final ModelDistanceService modelDistanceService;

    @Inject
    public DefaultGroupingService(ModelDistanceService modelDistanceService) {
        this.modelDistanceService = modelDistanceService;
    }

    @Override
    public void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

        int[] membership = ClusteringLPMs.cluster(
                lpmList,
                modelDistanceService.getDistanceMatrix(lpmList, config.getModelDistanceConfig()),
                config.getClusteringConfig().getClusteringAlgorithm(),
                config.getClusteringConfig().getClusteringParam());

        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty(config.getIdentifier(), membership[i]);
        }
    }
}
