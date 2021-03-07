package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;
//
//import org.processmining.placebasedlpmdiscovery.evaluation.results.concrete.WindowsEvaluationResult;
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class SubLPMFilter implements LPMFilter {
//
//    private double threshold;
//
//    public SubLPMFilter() {
//        this(0.05);
//    }
//
//    public SubLPMFilter(double threshold) {
//        this.threshold = threshold;
//    }
//
//    @Override
//    public Set<LocalProcessModel> filter(Set<LocalProcessModel> lpms) {
//        Set<LocalProcessModel> resLpms = new HashSet<>(); // create the resulting set
//
//        for (LocalProcessModel queryLpm : lpms) { // for every lpm
//            boolean isContained = false; // assume that is not contained in any other lpm
//            for (LocalProcessModel lpm : lpms) { // then iterate again through all the lpms
//                // and check first that we are not querying the same lpm
//                if (queryLpm.equals(lpm))
//                    continue;
//                // if this is not the case, check whether the lpm is duplicate of the query lpm
//                if (queryLpm.getPlaces().equals(lpm.getPlaces())) { // if they have the same set of places
//                    if (resLpms.contains(lpm)) { // and the other lpm is already included in the resulting set
//                        isContained = true; // mark the lpm as contained
//                        break; // and stop the iteration
//                    }
//                } // otherwise check whether the lpm is containing the query lpm
//                else if (lpm.containsLPM(queryLpm)
//                        && Math.abs( // and that the difference in the evaluation result is not larger than threshold
//                        lpm.getAdditionalInfo().getEvaluationResult()
//                                .getSimpleEvaluationResult(WindowsEvaluationResult.class).getResult() -
//                                queryLpm.getAdditionalInfo().getEvaluationResult()
//                                        .getSimpleEvaluationResult(WindowsEvaluationResult.class).getResult()) <= this.threshold) {
//                    isContained = true; // mark the query lpm as contained
//                    break; // stop the iteration since it is enough to be contained in only one lpm
//                }
//            }
//
//            if (!isContained) // if the lpm is not contained in any other lpm
//                resLpms.add(queryLpm); // add it in the resulting set
//        }
//
//        return resLpms; // return all lpms that are not contained in others
//    }
//}
