package org.processmining.placebasedlpmdiscovery.lpmdiscovery;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery;
//
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//import org.processmining.placebasedlpmdiscovery.model.Place;
//import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
//
//import java.util.List;
//import java.util.Set;
//
//public class LPMDiscoveryController {
//
//    private CombinationGuard guard;
//    private List<LPMFilter> filters;
//
//    public LPMDiscoveryController(CombinationGuard guard, List<LPMFilter> filters) {
//        this.guard = guard;
//        this.filters = filters;
//    }
//
//    public LPMResult discover(Set<Place> places, LPMCombinationParameters param) {
//        // discover local process models
//        LPMCombinationController controller = new LPMCombinationController(param);
//        controller.setCombinationGuard(this.guard);
//        controller.setAfterEvalFilters(this.filters);
//        Set<LocalProcessModel> finalLpms = controller.combine(places);
//
//        // create result
//        LPMResult result = new LPMResult();
//        for (LocalProcessModel lpm : finalLpms)
//            result.add(lpm);
//
//        return result;
//    }
//}
