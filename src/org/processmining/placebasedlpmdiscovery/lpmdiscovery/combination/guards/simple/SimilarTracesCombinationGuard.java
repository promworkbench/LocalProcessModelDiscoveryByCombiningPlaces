package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.combinationguards.simple;
//
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//import org.processmining.placebasedlpmdiscovery.model.Place;
//
///**
// * Guard which is satisfied if both the lpm and the place are fit for some percentage of the traces
// */
//public class SimilarTracesCombinationGuard extends CombinationGuardSimple {
//
//    private int percentage;
//
//    /**
//     *
//     * @param percentage: from 0 to 100
//     */
//    public SimilarTracesCombinationGuard(int percentage) {
//        this.percentage = percentage;
//    }
//
//    @Override
//    public boolean satisfies(LocalProcessModel lpm, Place place) {
//        int size = place.getAdditionalInfo().getTraceVariantInfo().length;
//        int counter = 0;
//
//        for (int i = 0; i < size; ++i) {
//            if (lpm.getAdditionalInfo().getTraceVariantInfos()[i].getFrequency() == place.getAdditionalInfo().getTraceVariantInfo()[i].getFrequency())
//                counter++;
//        }
//
//        return (counter * 100.0 / size) >= percentage;
//    }
//}
