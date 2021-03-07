package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;
//
//import org.processmining.placebasedlpmdiscovery.model.Place;
//import org.processmining.placebasedlpmdiscovery.model.Transition;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class NonExistingPathsPlaceFilter implements PlaceFilter {
//    @Override
//    public Set<Place> filter(Set<Place> places) {
//        for (Place place : places) { // for every place
//            Set<Transition> removalSet = new HashSet<>(); // create empty removal set for the input transitions
//            for (Transition inTr : place.getInputTransitions()) { // then for every input transition
//                boolean exists = false; // assume that a path containing that transition doesn't exist
//                for (Transition outTr : place.getOutputTransitions()) { // iterate through all output transitions
//                    // and check whether a path through the query input transition and the current output transition exists
//                    if (place.getAdditionalInfo().getPathInfo().get(getPathKey(inTr, outTr)) > 0) { // if it does
//                        exists = true; // mark that a path for that input transition exists
//                        break; // no need to check for other paths that contain that input transition
//                    }
//                }
//                if (!exists) { // if not path exists for the query input transition
//                    removalSet.add(inTr); // add the transition in the removal set
//                }
//            }
//            place.getInputTransitions().removeAll(removalSet); // remove all input transitions for which there is no path
//            for (Transition transition : removalSet) {
//                place.getAdditionalInfo().removePathsContainingTransition(true, transition);
//            }
//
//            // do the same for the output transitions
//            removalSet = new HashSet<>();
//            for (Transition outTr : place.getOutputTransitions()) {
//                boolean exists = false;
//                for (Transition inTr : place.getInputTransitions()) {
//                    if (place.getAdditionalInfo().getPathInfo() == null
//                            || place.getAdditionalInfo().getPathInfo().get(getPathKey(inTr, outTr)) == null)
//                        break;
//                    if (place.getAdditionalInfo().getPathInfo().get(getPathKey(inTr, outTr)) > 0) {
//                        exists = true;
//                    }
//                }
//                if (!exists) {
//                    removalSet.add(outTr);
//                }
//            }
//            place.getOutputTransitions().removeAll(removalSet);
//            for (Transition transition : removalSet) {
//                place.getAdditionalInfo().removePathsContainingTransition(false, transition);
//            }
//        }
//
//        return places; // return the place with removed input and output transitions for which there is no path containing them
//    }
//
//    private String getPathKey(Transition inTr, Transition outTr) {
//        return "(" + Long.toBinaryString(inTr.getMask()) + " | " + Long.toBinaryString(outTr.getMask()) + ")";
//    }
//}
