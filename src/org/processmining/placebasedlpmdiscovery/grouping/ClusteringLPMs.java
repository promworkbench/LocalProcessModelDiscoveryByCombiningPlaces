package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import smile.clustering.*;
import smile.clustering.linkage.*;
import smile.math.matrix.Matrix;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ClusteringLPMs {

    public static int[] cluster(List<LocalProcessModel> lpms,
                                double[][] proximity,
                                ClusteringAlgorithm clusteringAlgorithm,
                                Map<String, Object> config) {
        if (clusteringAlgorithm.equals(ClusteringAlgorithm.Hierarchical)) {
            return hierarchical(lpms, proximity, config);
        } else if (clusteringAlgorithm.equals(ClusteringAlgorithm.KMedoids)) {
            return kmedoids(lpms, proximity, config);
        } else if (clusteringAlgorithm.equals(ClusteringAlgorithm.DBSCAN)) {
            return dbscan(lpms, proximity, config);
        } else if (clusteringAlgorithm.equals(ClusteringAlgorithm.Spectral)) {
            return spectral(lpms, proximity, config);
        } else if (clusteringAlgorithm.equals(ClusteringAlgorithm.MinEntropy)) {
            return minentropy(lpms, proximity, config);
        }
        throw new IllegalArgumentException("No such clustering algorithm allowed");
    }

    private static int[] hierarchical(List<LocalProcessModel> lpms,
                                      double[][] proximity,
                                      Map<String, Object> config) {

        HierarchicalClustering hc = HierarchicalClustering.fit(getLinkage((String) config.get("linkage"), proximity));
        if (config.containsKey("num_clusters")) {
            return hc.partition((int) config.get("num_clusters"));
        } else if (config.containsKey("height")) {
            return hc.partition((double) config.get("height"));
        }
        throw new IllegalStateException("Neither num_clusters nor height is defined.");
    }

    private static Linkage getLinkage(String linkage, double[][] proximity) {
        switch (linkage) {
            case "single":
                return new SingleLinkage(proximity);
            case "complete":
                return new CompleteLinkage(proximity);
            case "upgma":
                return new UPGMALinkage(proximity);
            case "wpgma":
                return new WPGMALinkage(proximity);
        }
        throw new IllegalStateException("No such linkage is supported.");
    }

    private static int[] kmedoids(List<LocalProcessModel> lpms,
                                  double[][] proximity,
                                  Map<String, Object> config) {
        AtomicInteger counter = new AtomicInteger(0);
        Map<LocalProcessModel, Integer> lpmIndexMap = lpms.stream()
                .collect(Collectors.toMap(lpm -> lpm, lpm -> counter.getAndIncrement()));

        CLARANS<LocalProcessModel> clarans = PartitionClustering
                .run(20, () -> CLARANS.fit(
                        lpms.toArray(new LocalProcessModel[0]),
                        (lpm1, lpm2) -> proximity[lpmIndexMap.get(lpm1)][lpmIndexMap.get(lpm2)],
                        (int) config.get("num_clusters")));
        return clarans.y;
    }

    private static int[] dbscan(List<LocalProcessModel> lpms, double[][] proximity, Map<String, Object> config) {
        AtomicInteger counter = new AtomicInteger(0);
        Map<LocalProcessModel, Integer> lpmIndexMap = lpms.stream()
                .collect(Collectors.toMap(lpm -> lpm, lpm -> counter.getAndIncrement()));

        DBSCAN<LocalProcessModel> dbscan = DBSCAN.fit(
                lpms.toArray(new LocalProcessModel[0]),
                (lpm1, lpm2) -> proximity[lpmIndexMap.get(lpm1)][lpmIndexMap.get(lpm2)],
                (int) config.get("min_pts"),
                (double) config.get("radius"));
        return dbscan.y;
    }

    private static int[] spectral(List<LocalProcessModel> lpms, double[][] proximity, Map<String, Object> config) {
        SpectralClustering spectral = SpectralClustering.fit(Matrix.of(proximity), (int) config.get("num_clusters"));
        return spectral.y;
    }

    private static int[] minentropy(List<LocalProcessModel> lpms, double[][] proximity, Map<String, Object> config) {
        AtomicInteger counter = new AtomicInteger(0);
        Map<LocalProcessModel, Integer> lpmIndexMap = lpms.stream()
                .collect(Collectors.toMap(lpm -> lpm, lpm -> counter.getAndIncrement()));

        MEC<LocalProcessModel> mec = PartitionClustering
                .run(20, () -> MEC.fit(
                        lpms.toArray(new LocalProcessModel[0]),
                        (lpm1, lpm2) -> proximity[lpmIndexMap.get(lpm1)][lpmIndexMap.get(lpm2)],
                        (int) config.get("num_clusters"),
                        (double) config.get("radius")));
        return mec.y;
    }
}
