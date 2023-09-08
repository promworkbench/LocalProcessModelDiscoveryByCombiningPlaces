package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.CircularListWithMapping;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class WindowLPMTreeNode {

    private final int windowWidth;
    private final int position;
    private final int inEvent;
    private final int outEvent;
    private final ReplayableLocalProcessModel lpm;
    private final List<Integer> replayedEventsIndices;
    CircularListWithMapping<CircularListWithMapping<Set<WindowLPMTreeNode>, Integer>, Integer> children;
    private final UUID uuid;
    private WindowLPMTreeNode nullChild;

    public WindowLPMTreeNode(int position,
                             int windowWidth,
                             ReplayableLocalProcessModel lpm,
                             int inEvent,
                             int outEvent,
                             List<Integer> replayedEventsIndices) {
        this.uuid = UUID.randomUUID();
        this.position = position;
        this.windowWidth = windowWidth;
        this.lpm = lpm;

        this.inEvent = inEvent;
        this.outEvent = outEvent;

        this.children = new CircularListWithMapping<>(this.windowWidth);

        this.replayedEventsIndices = replayedEventsIndices;
    }

    public void tryAddPlace(int inEvent, int inPos, int outEvent, int outPos, Place place, Map<String, Integer> labelMap) {
        if (!canAddPlace(inEvent, place, labelMap))
            return;

        WindowLPMTreeNode child = createChild(inEvent, inPos, outEvent, outPos, place, labelMap);
        addChild(inEvent, inPos, outEvent, outPos, child);
    }

    public void tryAddPath(int inEvent, int inPos, int outEvent, int outPos, List<Place> path, Map<String, Integer> labelMap) {
        WindowLPMTreeNode child = createChild(inEvent, inPos, outEvent, outPos, path, labelMap);
        addChild(inEvent, inPos, outEvent, outPos, child);
    }

    private void addChild(int inEvent, int inPos, int outEvent, int outPos, WindowLPMTreeNode child) {
        child.tryAddNullChild(outEvent, outPos);
        if (this.children.isPosOld(inPos))
            return;
        CircularListWithMapping<Set<WindowLPMTreeNode>, Integer> placesForInPos = this.children.get(inPos);
        if (placesForInPos == null)
            placesForInPos = new CircularListWithMapping<>(this.windowWidth);
        Set<WindowLPMTreeNode> specificInOutChildren = placesForInPos.getOrDefault(outPos, new HashSet<>());
        specificInOutChildren.add(child);
        placesForInPos.set(outPos, specificInOutChildren, outEvent);
        this.children.set(inPos, placesForInPos, inEvent);
    }

    public void tryAddNullChild(int event, int eventPos) {
        ReplayableLocalProcessModel rlpm = new ReplayableLocalProcessModel(lpm);
        rlpm.fire(event);
        if (rlpm.isEmptyMarking())
            this.nullChild = new WindowLPMTreeNode(-1, this.windowWidth, rlpm, this.inEvent, this.outEvent,
                    getCopyOfUpdatedReplayedEventIndices(eventPos));
    }


    private WindowLPMTreeNode createChild(int event, int position, int outEvent, int outPos, Place place, Map<String, Integer> labelMap) {
        ReplayableLocalProcessModel newLpm = new ReplayableLocalProcessModel(this.lpm);
        addPlaceInRLPM(newLpm, event, place, labelMap);
        return new WindowLPMTreeNode(position, this.windowWidth, newLpm, event, outEvent,
                getCopyOfUpdatedReplayedEventIndices(position)); // create node
    }

    private WindowLPMTreeNode createChild(int event, int position, int outEvent, int outPos, List<Place> path, Map<String, Integer> labelMap) {
        ReplayableLocalProcessModel newLpm = new ReplayableLocalProcessModel(this.lpm);
        addPlaceInRLPM(newLpm, event, path.get(0), labelMap);
        Place previous = path.get(0);
        for (int i = 1; i < path.size(); ++i) {
            Place next = path.get(i);
            Transition silent = PlaceUtils.getCommonSilentTransition(previous, next);
            if (silent == null)
                throw new IllegalArgumentException("The path is not valid");
            addPlaceInRLPM(newLpm, labelMap.get(silent.getLabel()), next, labelMap);
            previous = next;
        }
        return new WindowLPMTreeNode(position, this.windowWidth, newLpm, event, outEvent,
                getCopyOfUpdatedReplayedEventIndices(position)); // create node
    }

    private void addPlaceInRLPM(ReplayableLocalProcessModel rlpm, int event, Place place, Map<String, Integer> labelMap) {
        // add transitions in the replayable lpm
        place.getInputTransitions().forEach(t -> rlpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));
        place.getOutputTransitions().forEach(t -> rlpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));

        // convert transitions in integers
        Set<Integer> inputTransitionIds = place.getInputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());
        Set<Integer> outputTransitionIds = place.getOutputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        rlpm.addConstraint(place.getId(), 0, outputTransitionIds, inputTransitionIds); // add the constraint
        rlpm.fire(event); // fire the event
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
    private boolean canAddPlace(Integer event, Place place, Map<String, Integer> labelMap) {
        Set<Integer> outputTransitionIds = place.getOutputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        Set<Integer> inputTransitionIds = place.getInputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        return Sets.intersection(outputTransitionIds, new HashSet<>(lpm.getFiringSequence())).isEmpty()
                && Sets.intersection(inputTransitionIds, new HashSet<>(lpm.getFiringSequence())).isEmpty()
                && !outputTransitionIds.contains(event);
    }

    public Collection<WindowLPMTreeNode> getChildren() {
        return this.children.getAll()
                .stream()
                .map(CircularListWithMapping::getAll)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Collection<WindowLPMTreeNode> getChildren(int pos) {
        Collection<WindowLPMTreeNode> res = new HashSet<>();
        for (int i = this.position; i <= pos; ++i) {
            if (this.children.isPosOld(i))
                continue;
            CircularListWithMapping<Set<WindowLPMTreeNode>, Integer> subChildren = this.children.get(i);
            if (subChildren == null)
                continue;
            res.addAll(subChildren.getAll().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        }
        return res;
    }

    public Collection<WindowLPMTreeNode> getChildren(int event, int pos) {
        Collection<WindowLPMTreeNode> res = new HashSet<>();
        for (int i = this.position; i < pos; ++i) {
            if (children.isPosOld(i)) continue;
            CircularListWithMapping<Set<WindowLPMTreeNode>, Integer> subChildren = this.children.get(i);
            if (subChildren == null)
                continue;
            for (int j = i + 1; j <= pos; ++j) {
                if (subChildren.isMapping(j, event)) {
                    if (subChildren.isPosOld(j))
                        continue;
                    res.addAll(subChildren.get(j));
                }
            }
        }
        return res;
    }

    public WindowLPMTreeNode getNullChild() {
        return nullChild;
    }

    public ReplayableLocalProcessModel getLpm() {
        return this.lpm;
    }

    public List<Integer> getReplayedEventsIndices() {
        return replayedEventsIndices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowLPMTreeNode that = (WindowLPMTreeNode) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    private List<Integer> getCopyOfUpdatedReplayedEventIndices(int eventPos) {
        List<Integer> tempEventIndices = new ArrayList<>(this.replayedEventsIndices);
        tempEventIndices.add(eventPos);
        return tempEventIndices;
    }
}
