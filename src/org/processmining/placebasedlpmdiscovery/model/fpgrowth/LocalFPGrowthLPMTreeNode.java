package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalFPGrowthLPMTreeNode {
    private final UUID uuid;
    private final ReplayableLocalProcessModel lpm;
    private final LocalFPGrowthLPMTreeNode parent;
    private final Set<LocalFPGrowthLPMTreeNode> children;
    private final Set<UUID> nullAncestors;
    private final Set<Integer> offLimitTransitions;
    private final Map<Integer, Boolean> knownEvents;

    public LocalFPGrowthLPMTreeNode() {
        this.uuid = UUID.randomUUID();
        this.lpm = new ReplayableLocalProcessModel();
        this.parent = null;
        this.children = new HashSet<>();
        this.nullAncestors = new HashSet<>();
        this.offLimitTransitions = new HashSet<>();
        this.knownEvents = new HashMap<>();
    }

    public LocalFPGrowthLPMTreeNode(ReplayableLocalProcessModel lpm, Set<Integer> offLimitTransitions,
                                    LocalFPGrowthLPMTreeNode parent) {
        this.uuid = UUID.randomUUID();
        this.lpm = lpm;
        this.parent = parent;
        this.children = new HashSet<>();
        this.nullAncestors = new HashSet<>();
        this.offLimitTransitions = new HashSet<>(offLimitTransitions);
        this.knownEvents = new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<UUID> getNullAncestors() {
        return nullAncestors;
    }

    public ReplayableLocalProcessModel getLpm() {
        return lpm;
    }

    public Set<Integer> getOffLimitTransitions() {
        return offLimitTransitions;
    }

    public Map<Integer, Boolean> getKnownEvents() {
        return knownEvents;
    }

    public LocalFPGrowthLPMTreeNode addChild(Integer event, Place place, Map<String, Integer> labelMap) {
        ReplayableLocalProcessModel newLpm = new ReplayableLocalProcessModel(this.lpm);

        // add transitions in the replayable lpm
        place.getInputTransitions().forEach(t -> newLpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));
        place.getOutputTransitions().forEach(t -> newLpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));

        // convert transitions in integers
        Set<Integer> inputTransitionIds = place.getInputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());
        Set<Integer> outputTransitionIds = place.getOutputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        newLpm.addConstraint(0, outputTransitionIds, inputTransitionIds); // add the constraint
        newLpm.fire(event); // fire the event
        LocalFPGrowthLPMTreeNode child = new LocalFPGrowthLPMTreeNode(newLpm, this.offLimitTransitions, this); // create node
        child.offLimitTransitions.add(event); // add the fired event as off limit transition
        children.add(child); // add it as child
        return child;
    }

    public boolean canLPMFire(Integer event) {
        if (this.knownEvents.containsKey(event))
            return this.knownEvents.get(event);
        boolean canFire = this.lpm.canFire(event);
        this.knownEvents.put(event, canFire);
        return canFire;
    }

    public boolean isEmptyWhenFire(Integer event) {
        ReplayableLocalProcessModel newLpm = new ReplayableLocalProcessModel(this.lpm);
        return newLpm.fire(event) && newLpm.isEmptyMarking();
    }

    public LocalFPGrowthLPMTreeNode addNullChild(Integer event) {
        ReplayableLocalProcessModel newLpm = new ReplayableLocalProcessModel(this.lpm);
        newLpm.fire(event); // fire the event
        LocalFPGrowthLPMTreeNode child = new LocalFPGrowthLPMTreeNode(newLpm, this.offLimitTransitions, this); // create the node
        children.add(child); // add it is child
        this.registerNullChild(child.uuid);
        return child;
    }

    private void registerNullChild(UUID nullChildUUID) {
        this.nullAncestors.add(nullChildUUID);
        if (parent != null)
            parent.registerNullChild(nullChildUUID);
    }

    /**
     * Checks whether we add a constraint to some of the transitions we have already replayed
     * or if the event we want to replay has the place we want to add as a constraint
     *
     * @param event:    the event we want to replay
     * @param place:    the place we want to add
     * @param labelMap: the mapping between the string and integer labels
     * @return whether the place can be added without destroying the previous replay
     */
    public boolean canAddChild(Integer event, Place place, Map<String, Integer> labelMap) {
        Set<Integer> outputTransitionIds = place.getOutputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        return Sets.intersection(outputTransitionIds, this.offLimitTransitions).isEmpty()
                && !outputTransitionIds.contains(event);
    }

    public Set<LocalFPGrowthLPMTreeNode> getAllDescendants() {
        return children.stream()
                .flatMap(n -> Stream.concat(Stream.of(n), n.getAllDescendants().stream()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalFPGrowthLPMTreeNode that = (LocalFPGrowthLPMTreeNode) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
