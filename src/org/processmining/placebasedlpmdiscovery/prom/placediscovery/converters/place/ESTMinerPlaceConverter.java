package org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.TransitionUtils;
import org.processmining.v7.postproc_after_tc.MyPlace;
import org.processmining.v7.postproc_after_tc.MyProcessModel;
//import org.processmining.v8.eSTMinerGIT.MyPlace;
//import org.processmining.v8.eSTMinerGIT.MyProcessModel;

import java.util.*;

public class ESTMinerPlaceConverter extends AbstractPlaceConverter<MyProcessModel> {

    @Override
    public Set<Place> convert(MyProcessModel result) {
        Set<Place> discoveredPlaces = new HashSet<>();
        Set<Transition> transitions = TransitionUtils.createVisibleTransitionsForGivenNames(result.getTransitions());
        Map<String, Transition> labelTransitionMap = TransitionUtils.mapLabelsIntoTransitions(transitions);
        for (MyPlace place : result.getPlaces())
            discoveredPlaces.add(convertPlace(place, result.getTransitions(), labelTransitionMap));

        return discoveredPlaces;
    }

    /**
     * Converts a place from the ESTMiner into a place that can be used by this plugin
     *
     * @param place:           a place that is returned from the ESTMiner and needs to be converted
     * @param transitionNames: array containing the names of the transitions that can be connected to a place
     * @return a place that can be used by the code in this plugin
     */
    private Place convertPlace(MyPlace place, String[] transitionNames, Map<String, Transition> transitionMap) {
        Place resPlace = new Place();

        Collection<String> inputTransitionNames = getTransitionNames(place.getInputTrKey(), transitionNames);
        if (inputTransitionNames != null)
            for (String inTr : inputTransitionNames) {
                Transition transition = new Transition(inTr, false);
                if (transitionMap.containsKey(inTr))
                    transition = transitionMap.get(inTr);

                resPlace.addInputTransition(transition);
            }

        Collection<String> outputTransitionNames = getTransitionNames(place.getOutputTrKey(), transitionNames);
        if (outputTransitionNames != null)
            for (String oTr : outputTransitionNames) {
                Transition transition = new Transition(oTr, false);
                if (transitionMap.containsKey(oTr))
                    transition = transitionMap.get(oTr);

                resPlace.addOutputTransition(transition);
            }

//        resPlace.setAdditionalInfo(DetailedPlaceInfoConverter.convert(place.getPlaceInfo()));
//        resPlace.getAdditionalInfo().setPathInfo(place.getPathCountingInfo().getPlacePathFrequencyMap());

        return resPlace;
    }

    /**
     * Returns a collection containing all transitions names from the given transitions array
     */
    private Collection<String> getTransitionNames(final int key, final String[] transitions) {
        Collection<String> result = new ArrayList<>();
        if (key > (Math.pow(2, transitions.length))) {
            return null;
        }
        for (int i = 0; i < transitions.length; i++) {
            if ((key & getMask(i, transitions)) > 0) { //test key for ones
                result.add(transitions[i]);
            }
        }
        return result;
    }

    /**
     * For a given position in the transition array return the corresponding bitmask
     */
    private int getMask(final int pos, final String[] transitions) {
        return 1 << (transitions.length - 1 - pos);
    }
}
