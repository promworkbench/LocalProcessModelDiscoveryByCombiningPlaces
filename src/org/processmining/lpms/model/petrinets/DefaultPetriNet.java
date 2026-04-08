package org.processmining.lpms.model.petrinets;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.processmining.lpms.model.petrinets.nodes.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DefaultPetriNet implements PetriNet {

    private final Collection<Place> places;
    private final Collection<Transition> transitions;
    private final BiMap<Node, NodePosition> nodePositions;
    private final DirectedGraph<Node, Arc> graph;

    public DefaultPetriNet() {
        this.places = new HashSet<>();
        this.transitions = new HashSet<>();
        this.nodePositions = HashBiMap.create();
        this.graph = new SimpleDirectedGraph<>(Arc::of);
    }

    public DefaultPetriNet(Collection<Place> places, Collection<Transition> transitions) {
        this.places = places;
        this.transitions = transitions;
        this.nodePositions = HashBiMap.create();
        this.graph = new SimpleDirectedGraph<>(Arc::of);
    }

    @Override
    public Collection<Place> getPlaces() {
        return this.places;
    }

    @Override
    public Collection<Transition> getTransitions() {
        return this.transitions;
    }

    @Override
    public Collection<Arc> getArcsFrom(Node node) {
        return graph.outgoingEdgesOf(node);
    }

    @Override
    public Collection<Arc> getArcsTo(Node node) {
        return graph.incomingEdgesOf(node);
    }

    @Override
    public Collection<Node> getPreset(Node node) {
        return getArcsTo(node).stream().map(Arc::getSource).collect(Collectors.toSet());
    }

    @Override
    public Collection<Node> getPostset(Node node) {
        return getArcsFrom(node).stream().map(Arc::getTarget).collect(Collectors.toSet());
    }

    @Override
    public Place addPlace() {
        Place place = new DefaultPlace();
        this.places.add(place);
        this.graph.addVertex(place);
        return place;
    }

    @Override
    public Place addPlace(Collection<Transition> inputTransitions, Collection<Transition> outputTransitions) {
        Place place = addPlace();
        inputTransitions.forEach(inputTransition -> this.graph.addEdge(inputTransition, place));
        outputTransitions.forEach(outputTransition -> this.graph.addEdge(place, outputTransition));
        return place;
    }

    @Override
    public Transition addTransition(String label) {
        Transition transition = Transition.of(label);
        if (!addTransition(transition)) {
            return null;
        }
        return transition;
    }

    @Override
    public Transition addInvisibleTransition() {
        Transition transition = Transition.invisible();
        if (!addTransition(transition)) {
            return null;
        }
        return transition;
    }

    private boolean addTransition(Transition transition) {
        if (transition == null) {
            return false;
        }
        this.transitions.add(transition);
        this.graph.addVertex(transition);
        return true;
    }

    @Override
    public Transition addTransition(Collection<Place> inputPlaces, Collection<Place> outputPlaces, String label) {
        Transition transition = addTransition(label);
        if (transition == null) {
            return null;
        }
        inputPlaces.forEach(inputPlace -> this.graph.addEdge(inputPlace, transition));
        outputPlaces.forEach(outputPlace -> this.graph.addEdge(transition, outputPlace));
        return transition;
    }

    @Override
    public Transition addInvisibleTransition(Collection<Place> inputPlaces, Collection<Place> outputPlaces) {
        Transition transition = addInvisibleTransition();
        if (transition == null) {
            return null;
        }
        inputPlaces.forEach(inputPlace -> this.graph.addEdge(inputPlace, transition));
        outputPlaces.forEach(outputPlace -> this.graph.addEdge(transition, outputPlace));
        return transition;
    }

    @Override
    public NodePosition getNodePosition(Node node) {
        return this.nodePositions.get(node);
    }
}
