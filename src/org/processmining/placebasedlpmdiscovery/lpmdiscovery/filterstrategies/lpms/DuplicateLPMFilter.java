package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;
//
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class DuplicateLPMFilter implements LPMFilter {
//
//    @Override
//    public Set<LocalProcessModel> filter(Set<LocalProcessModel> lpms) {
//        Set<LocalProcessModel> resSet = new HashSet<>();
//        for (LocalProcessModel query : lpms) {
//            boolean contained = false;
//            for (LocalProcessModel lpm : lpms) {
//                if (query == lpm)
//                    continue;
//                if (query.equals(lpm) && resSet.contains(lpm)) {
//                    contained = true;
//                    break;
//                }
//            }
//            if (!contained)
//                resSet.add(query);
//            else
//                System.out.println("jumped");
//        }
//        return resSet;
//    }
//}
