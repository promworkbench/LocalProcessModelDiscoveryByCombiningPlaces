package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GroupingController {

    public void groupLPMs(Collection<LocalProcessModel> lpms, Map<String, Object> config) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

        int[] membership = ClusteringLPMs.cluster(
                lpmList,
                computeProximity(lpmList, config),
                getClusteringAlgorithm(config),
                config);

        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty((String) config.get(Constants.Grouping.Config.TITLE), membership[i]);
        }
    }

    private double[][] computeProximity(List<LocalProcessModel> lpmList, Map<String, Object> config) {
        ModelDistanceController distanceController = new ModelDistanceController(
                config.get(Constants.Grouping.Config.DISTANCE_METHOD).toString(),
                (Map<String, String>) config.get(Constants.Grouping.Config.DISTANCE_CONFIG));

        return distanceController.getDistanceMatrix(lpmList);
    }

    private ClusteringAlgorithm getClusteringAlgorithm(Map<String, Object> config) {
        return ClusteringAlgorithm.valueOf((String) config.get(Constants.Grouping.Config.CLUSTERING_ALG));
    }
}
