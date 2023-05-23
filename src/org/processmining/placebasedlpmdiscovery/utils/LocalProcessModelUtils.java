package org.processmining.placebasedlpmdiscovery.utils;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Arc;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.models.graphbased.AbstractGraphElement;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalProcessModelUtils {

    public static Set<LocalProcessModel> convertPlacesToLPMs(Set<Place> places) {
        Set<LocalProcessModel> res = new HashSet<>();
        for (Place place : places) {
            res.add(new LocalProcessModel(place));
        }

        return res;
    }

    public static ReplayableLocalProcessModel convertToReplayable(LocalProcessModel lpm,
                                                                  Map<String, Integer> labelMap) {
        Set<Integer> transitionsMapped = lpm.getTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());
        Set<Integer> invisibleTransitions = lpm.getTransitions()
                .stream()
                .filter(Transition::isInvisible)
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        ReplayableLocalProcessModel replayable = new ReplayableLocalProcessModel(transitionsMapped, invisibleTransitions);
        for (Place p : lpm.getPlaces()) {
            Set<Integer> inputTransitionIds = p.getInputTransitions()
                    .stream()
                    .map(t -> labelMap.get(t.getLabel()))
                    .collect(Collectors.toSet());
            Set<Integer> outputTransitionIds = p.getOutputTransitions()
                    .stream()
                    .map(t -> labelMap.get(t.getLabel()))
                    .collect(Collectors.toSet());
            replayable.addConstraint(p.getId(), p.getNumTokens(), outputTransitionIds, inputTransitionIds);
        }

        return replayable;
    }

    public static LocalProcessModel convertReplayableToLPM(ReplayableLocalProcessModel replayable,
                                                           Map<Integer, String> reversedLabelMap) {
        // create all transitions
        Map<Integer, Transition> transitions = new HashMap<>();
        Set<Integer> transitionsMapped = replayable.getTransitions();
        Set<Integer> invisibleTransitionsMapped = replayable.getInvisibleTransitions();
        for (Integer tr : transitionsMapped) {
            boolean invisible = invisibleTransitionsMapped.contains(tr);
            Transition transition = new Transition(reversedLabelMap.get(tr), invisible);
            transitions.put(tr, transition);
        }

        // create places for each constraint
        Map<Integer, Place> constraintIdPlaceMap = replayable.getConstraintMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new Place(e.getValue()))); // TODO: Here the sent places should be used
        // add output transitions to the places
        for (Map.Entry<Integer, Set<Integer>> entry : replayable.getInputConstraints().entrySet()) {
            int trId = entry.getKey();
            entry.getValue().forEach(id -> constraintIdPlaceMap.get(id).addOutputTransition(transitions.get(trId)));
        }
        // add input transitions to the places
        for (Map.Entry<Integer, Set<Integer>> entry : replayable.getOutputConstraints().entrySet()) {
            int trId = entry.getKey();
            entry.getValue().forEach(id -> constraintIdPlaceMap.get(id).addInputTransition(transitions.get(trId)));
        }

        // create the lpm
        LocalProcessModel lpm = new LocalProcessModel();
        for (Place p : constraintIdPlaceMap.values())
            lpm.addPlace(p);

        return lpm;
    }

    public static LocalProcessModel revertLocalProcessModel(LocalProcessModel lpm) {
        LocalProcessModel reverted = new LocalProcessModel();

        for (Place place : lpm.getPlaces()) {
            reverted.addPlace(PlaceUtils.revertPlace(place));
        }

        return reverted;
    }

    public static AcceptingPetriNet getAcceptingPetriNetRepresentation(LocalProcessModel lpm) {

        Petrinet net = LocalProcessModelUtils.getPetriNetRepresentation(lpm);

        Marking initial = new Marking();
        Marking finalM = new Marking();

        Map<String, org.processmining.models.graphbased.directed.petrinet.elements.Place>
                placeMap = net.getPlaces().stream().collect(Collectors.toMap(AbstractGraphElement::getLabel, x -> x));

        for (Place place : lpm.getPlaces()) {
            if (place.getNumTokens() > 0)
                initial.add(placeMap.get(place.getId()), place.getNumTokens());
        }

        return new AcceptingPetriNetImpl(net, initial, finalM);
    }

    public static Petrinet getPetriNetRepresentation(LocalProcessModel lpm) {
        Petrinet net = PetrinetFactory.newPetrinet(lpm.getId());

        for (Place p : lpm.getPlaces())
            net.addPlace(p.getId());

        for (Transition t : lpm.getTransitions()) {
            org.processmining.models.graphbased.directed.petrinet.elements.Transition netTransition = net.addTransition(t.getLabel());
            netTransition.setInvisible(t.isInvisible());
        }

        Map<String, org.processmining.models.graphbased.directed.petrinet.elements.Place>
                placeMap = new HashMap<>();

        Map<String, org.processmining.models.graphbased.directed.petrinet.elements.Transition>
                transitionMap = new HashMap<>();

        try {
//            Map<String, org.processmining.models.graphbased.directed.petrinet.elements.Place>
            placeMap = net.getPlaces().stream().collect(Collectors.toMap(AbstractGraphElement::getLabel, x -> x));
//            Map<String, org.processmining.models.graphbased.directed.petrinet.elements.Transition>
            transitionMap = net.getTransitions().stream().collect(Collectors.toMap(AbstractGraphElement::getLabel, x -> x));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Arc arc : lpm.getArcs()) {
            if (arc.isInput())
                net.addArc(transitionMap.get(arc.getTransition().getLabel()),
                        placeMap.get(arc.getPlace().getId()));
            else
                net.addArc(placeMap.get(arc.getPlace().getId()),
                        transitionMap.get(arc.getTransition().getLabel()));
        }

        return net;
    }

    public static Set<String> getPossiblePassages(LocalProcessModel lpm, Map<String, ?> labelMap) {
        Set<String> allPossiblePassages = new HashSet<>();
        for (Place place : lpm.getPlaces()) {
            for (Transition inTr : place.getInputTransitions())
                for (Transition outTr : place.getOutputTransitions()) {
                    allPossiblePassages.add(labelMap.get(inTr.getLabel()) + "-" + labelMap.get(outTr.getLabel()));
                }
        }
        return allPossiblePassages;
    }

    public static Set<String> getPossiblePassages(LocalProcessModel lpm) {
        Map<String, String> identityLabelMap = lpm.getTransitions()
                .stream()
                .collect(Collectors.toMap(Transition::getLabel, Transition::getLabel));
        return LocalProcessModelUtils.getPossiblePassages(lpm, identityLabelMap);
    }

    public static LocalProcessModel getLPMFromAcceptingPetriNetRepresentation(AcceptingPetriNet net) {
        LocalProcessModel lpm = new LocalProcessModel();

        Map<String, Transition> transitionMap = net.getNet().getTransitions()
                .stream()
                .collect(Collectors.toMap(AbstractGraphElement::getLabel, x -> new Transition(x.getLabel(), x.isInvisible())));

        for (org.processmining.models.graphbased.directed.petrinet.elements.Place p : net.getNet().getPlaces()) {

            Place place = new Place();
            place.setNumTokens(net.getInitialMarking().occurrences(p));

            for (PetrinetEdge<?, ?> edge : net.getNet().getInEdges(p)) {
                place.addInputTransition(transitionMap.get(edge.getSource().getLabel()));
            }

            for (PetrinetEdge<?, ?> edge : net.getNet().getOutEdges(p)) {
                place.addOutputTransition(transitionMap.get(edge.getTarget().getLabel()));
            }

            lpm.addPlace(place);
        }
        return lpm;
    }

    public static LocalProcessModel join(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        LocalProcessModel lpm = new LocalProcessModel(lpm1);
        lpm.addAllPlaces(lpm2.getPlaces());
        return lpm;
    }
}
