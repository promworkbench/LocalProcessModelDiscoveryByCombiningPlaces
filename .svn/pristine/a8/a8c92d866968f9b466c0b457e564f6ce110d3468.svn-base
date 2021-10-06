package org.processmining.placebasedlpmdiscovery.utils;

import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility functions that can be used over transitions
 */
public class TransitionUtils {

    /**
     * Creates a mapping from the transition labels to the transition
     *
     * @param transitions: set of transitions for which the mapping is created
     * @return map of labels to transitions
     */
    public static Map<String, Transition> mapLabelsIntoTransitions(Set<Transition> transitions) {
        Map<String, Transition> transitionMap = new HashMap<>();
        for (Transition transition : transitions) {
            transitionMap.put(transition.getLabel(), transition);
        }
        return transitionMap;
    }

    /**
     * Creates a mapping from the transition id to the transition
     *
     * @param transitions: set of transitions for which the mapping is created
     * @return map of ids to transitions
     */
    public static Map<String, Transition> mapIdsIntoTransitions(Set<Transition> transitions) {
        Map<String, Transition> transitionMap = new HashMap<>();
        for (Transition transition : transitions) {
            transitionMap.put(transition.getId(), transition);
        }
        return transitionMap;
    }

    /**
     * Creates transitions for every transition name
     *
     * @param transitionNames: array of transition names for which transitions are created
     * @return set of transitions
     */
    public static Set<Transition> createVisibleTransitionsForGivenNames(String[] transitionNames) {
        Set<Transition> transitions = new HashSet<>();
        for (String name : transitionNames) {
            transitions.add(new Transition(name, false));
        }

        return transitions;
    }

//    public static int getMask(int position, int size) {
//        return (1 << (size - 1 - position));
//    }
//
//    public static int getIndex(String label, String[] transitionNames) {
//        for (int i = 0; i < transitionNames.length; ++i) {
//            if (label.equals(transitionNames[i]))
//                return i;
//        }
//        return -1;
//    }
}
