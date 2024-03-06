package org.processmining.placebasedlpmdiscovery.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupingController {

    private final ModelDistanceController modelDistanceController;

    @Inject
    public GroupingController(ModelDistanceController modelDistanceController) {
        this.modelDistanceController = modelDistanceController;
    }

    public void groupLPMs(Collection<LocalProcessModel> lpms, GroupingConfig config) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

        int[] membership = ClusteringLPMs.cluster(
                lpmList,
                modelDistanceController.getDistanceMatrix(lpmList),
                config.getClusteringAlgorithm(),
                config.getClusteringConfig());

        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty(config.getIdentifier(), membership[i]);
        }
    }

}
