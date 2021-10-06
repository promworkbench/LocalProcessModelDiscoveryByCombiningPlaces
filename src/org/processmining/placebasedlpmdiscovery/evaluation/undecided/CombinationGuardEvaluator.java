package org.processmining.placebasedlpmdiscovery.evaluation.undecided;//package org.processmining.placebasedlpmdiscovery.evaluation.undecided;
//
//import org.deckfour.xes.model.XLog;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscoveryController;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.combinationguards.CombinationGuard;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
//import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
//import org.processmining.placebasedlpmdiscovery.model.Place;
//import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class CombinationGuardEvaluator {
//
//    private CombinationGuard guard;
//    private Set<LPMFilter> filters;
//
//    public CombinationGuardEvaluator(CombinationGuard guard, Set<LPMFilter> filters) {
//        this.guard = guard;
//        this.filters = filters;
//    }
//
//    public Set<CombinationGuardEvaluationResults> evaluate(List<Set<Place>> listOfPlaces, List<XLog> listOfLogs) {
//        Set<CombinationGuardEvaluationResults> evaluationResults = new HashSet<>();
//        for (int i = 0; i < listOfPlaces.size(); ++i) {
//            evaluationResults.add(this.evaluateSingle(listOfPlaces.get(i), listOfLogs.get(i)));
//        }
//
//        return evaluationResults;
//    }
//
//    public CombinationGuardEvaluationResults evaluateSingle(Set<Place> places, XLog log) {
//        CombinationGuardEvaluationResults results = new CombinationGuardEvaluationResults();
//
//        // filter places
//        places = PlaceDiscovery.filterPlaces(places);
//
//        // discover local process models
//        LPMDiscoveryController discoveryController = new LPMDiscoveryController(this.guard, this.filters);
//        LPMCombinationParameters parameters =
//                new LPMCombinationParameters(2, 5, 0.2);
//        LPMResult lpms = discoveryController.discover(places, parameters);
////        Set<org.processmining.placebasedlpmdiscovery.model.LocalProcessModel> forRemoval = new HashSet<>();
////        for (int i = 0; i < lpms.getSize(); ++i) {
////            if (lpms.getLPM(i).getInputTransitions().size() < 1 || lpms.getLPM(i).getOutputTransitions().size() < 1)
////                forRemoval.add(lpms.getLPM(i));
////        }
////        for (org.processmining.placebasedlpmdiscovery.model.LocalProcessModel lpm : forRemoval) {
////            lpms.removeLPM(lpm);
////        }
//
//        lpms.sort();
//
//        // evaluate discovered local process models
//        LocalProcessModelEvaluator evaluator = new LocalProcessModelEvaluator();
//        evaluator.evaluate(lpms, log);
//
//        for (int i = 0; i < lpms.getSize(); ++i) {
//            results.addResult(lpms.getLPM(i).toString(), lpms.getLPM(i).getEvaluationResults());
//        }
//
//        return results;
//    }
//
//    public CombinationGuardEvaluationResults evaluateAggregate(List<Set<Place>> listOfPlaces, List<XLog> listOfLogs) throws Exception {
//        CombinationGuardEvaluationResults aggregateResults = new CombinationGuardEvaluationResults();
//
//        Set<CombinationGuardEvaluationResults> evaluationResults = this.evaluate(listOfPlaces, listOfLogs);
//        for (CombinationGuardEvaluationResults results : evaluationResults) {
//            // TODO: aggregate results here
//        }
//
//        return aggregateResults;
//    }
//
//    public CombinationGuardEvaluationResults evaluateSingle(XLog log) throws Exception {
//        Set<Place> places = PlaceDiscovery.discover(log);
//        return this.evaluateSingle(places, log);
//    }
//}
