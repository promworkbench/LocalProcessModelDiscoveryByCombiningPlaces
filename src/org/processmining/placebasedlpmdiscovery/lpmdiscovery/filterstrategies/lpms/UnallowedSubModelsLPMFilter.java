package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;
//
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class UnallowedSubModelsLPMFilter implements LPMFilter {
//
//    private Set<LocalProcessModel> unallowedLPMs;
//
//    public UnallowedSubModelsLPMFilter() {
//        this.unallowedLPMs = new HashSet<>();
//    }
//
//
//    public void addAllUnallowedLPMs(Collection<LocalProcessModel> lpms) {
//        this.unallowedLPMs.addAll(lpms);
//    }
//
//    @Override
//    public Set<LocalProcessModel> filter(Set<LocalProcessModel> lpms) {
//        Set<LocalProcessModel> resSet = new HashSet<>();
//        for (LocalProcessModel lpm : lpms) {
//            boolean contain = false;
//            for (LocalProcessModel unallowed : this.unallowedLPMs) {
//                if (lpm.containsLPM(unallowed)) {
//                    contain = true;
//                    break;
//                }
//            }
//            if (!contain)
//                resSet.add(lpm);
//        }
//        return resSet;
//    }
//}
