package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ReplayableLocalProcessModel {
    // TODO: Maybe between the transitions and constraints, weights can be added
    // TODO: It doesn't support duplicate transitions
    private int constraintIdentifier;
    private final Map<Integer, Constraint> constraintMap; // every constraint has an integer id
    private final Map<Integer, Set<Integer>> inputConstraints; // every transition has a set of input constraints
    private final Map<Integer, Set<Integer>> outputConstraints; // every transition has a set of output constraints
    private final Set<Integer> transitions; // every transition has an integer id
    private final Set<Integer> invisibleTransitions; // some transitions are invisible
    private boolean hasFired;
    private final List<Integer> firingSequence;
    private final Set<Pair<Integer, Integer>> usedPassages;
    private final Set<Integer> usedConstraints;

    private final Set<Integer> invisibleFired; // set that is used so that invisible transitions do not create infinite recursion
    private int silentFiresCount;

    public ReplayableLocalProcessModel() {
        this.transitions = new HashSet<>();
        this.invisibleTransitions = new HashSet<>();
        this.constraintMap = new HashMap<>();
        this.inputConstraints = new HashMap<>();
        this.outputConstraints = new HashMap<>();
        this.constraintIdentifier = 1;
        this.invisibleFired = new HashSet<>();
        this.firingSequence = new ArrayList<>();
        this.usedPassages = new HashSet<>();
        this.usedConstraints = new HashSet<>();
    }

    public ReplayableLocalProcessModel(Set<Integer> transitions, Set<Integer> invisibleTransitions) {
        this.transitions = transitions;
        this.invisibleTransitions = invisibleTransitions;
        this.constraintMap = new HashMap<>();
        this.inputConstraints = new HashMap<>();
        this.outputConstraints = new HashMap<>();
        this.constraintIdentifier = 1;
        this.invisibleFired = new HashSet<>();
        this.firingSequence = new ArrayList<>();
        this.usedPassages = new HashSet<>();
        this.usedConstraints = new HashSet<>();
    }

    public ReplayableLocalProcessModel(ReplayableLocalProcessModel rlpm) {
        this.transitions = new HashSet<>(rlpm.transitions);
        this.invisibleTransitions = new HashSet<>(rlpm.invisibleTransitions);
        this.constraintMap = rlpm.constraintMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> new Constraint(x.getValue())));
        this.inputConstraints = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : rlpm.inputConstraints.entrySet()) {
            this.inputConstraints.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        this.outputConstraints = new HashMap<>(rlpm.outputConstraints);
        for (Map.Entry<Integer, Set<Integer>> entry : rlpm.outputConstraints.entrySet()) {
            this.outputConstraints.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        this.constraintIdentifier = rlpm.constraintIdentifier;

//        this.paths = new HashSet<>(rlpm.paths);
        this.hasFired = rlpm.hasFired;
        this.firingSequence = new ArrayList<>(rlpm.getFiringSequence());
        this.usedPassages = new HashSet<>(rlpm.getUsedPassages());
        this.usedConstraints = new HashSet<>(rlpm.usedConstraints);
        this.silentFiresCount = rlpm.silentFiresCount;
        this.invisibleFired = new HashSet<>(rlpm.invisibleFired);
    }

    public Set<Pair<Integer, Integer>> getUsedPassages() {
        return this.usedPassages;
    }

    public Set<SimplePlace<Integer>> getUsedConstraints() {
        // initialize inputs and outputs for each constraint
        Map<Integer, Set<Integer>> inputs = new HashMap<>();
        Map<Integer, Set<Integer>> outputs = new HashMap<>();
        this.usedConstraints.forEach(c -> {
            inputs.put(c, new HashSet<>());
            outputs.put(c, new HashSet<>());
        });

        // add inputs and outputs
        this.inputConstraints.forEach((t, cs) -> {
            Set<Integer> incs = new HashSet<>(cs);
            incs.retainAll(this.usedConstraints);
            incs.forEach(c -> outputs.get(c).add(t)); // for the input constraints of t, t is an output transition
        });
        this.outputConstraints.forEach((t, cs) -> {
            Set<Integer> outcs = new HashSet<>(cs);
            outcs.retainAll(this.usedConstraints);
            outcs.forEach(c -> inputs.get(c).add(t)); // for the output constraints of t, t is an input transition
        });

        // return used constraints in short string form representation
        return this.usedConstraints.stream()
                .map(c -> new SimplePlace<>(inputs.get(c), outputs.get(c)))
                .collect(Collectors.toSet());
    }

    public List<Integer> getFiringSequence() {
        return this.firingSequence;
    }

    public Map<Integer, String> getConstraintMap() {
        return constraintMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getId()));
    }

    public Set<Integer> getTransitions() {
        return transitions;
    }

    public Set<Integer> getInvisibleTransitions() {
        return invisibleTransitions;
    }

    public Map<Integer, Set<Integer>> getInputConstraints() {
        return inputConstraints;
    }

    public Map<Integer, Set<Integer>> getOutputConstraints() {
        return outputConstraints;
    }

    public void addTransition(Integer transition, boolean invisible) {
        if (invisible)
            this.invisibleTransitions.add(transition);
        this.transitions.add(transition);
    }

    /**
     * Add constraint in the local process model
     *
     * @param numTokens:              the number of tokens the constraint contains
     * @param haveAsInputConstraint:  the transition ids that have the constraint as input constraint
     * @param haveAsOutputConstraint: the transition ids that have the constraint as output constraint
     */
    public void addConstraint(String id, int numTokens, Set<Integer> haveAsInputConstraint, Set<Integer> haveAsOutputConstraint) {
//        // check if the transitions were added previously
//        if (!Sets.union(this.transitions, this.invisibleTransitions)
//                .containsAll(Sets.union(haveAsInputConstraint, haveAsOutputConstraint))) {
//            throw new IllegalStateException("All transitions should be previously added to the model");
//        }

        Constraint constraint = new Constraint(id, numTokens);
        // add it in the constraint map
        this.constraintMap.put(this.constraintIdentifier, constraint);

        // add all transitions that have it as input constraint
        for (Integer trId : haveAsInputConstraint) {
            Set<Integer> trInputConstraints = new HashSet<>();
            if (this.inputConstraints.containsKey(trId))
                trInputConstraints = this.inputConstraints.get(trId);
            trInputConstraints.add(this.constraintIdentifier);
            this.inputConstraints.put(trId, trInputConstraints);
        }
        // add all transitions that have it as output constraint
        for (Integer trId : haveAsOutputConstraint) {
            Set<Integer> trOutputConstraints = new HashSet<>();
            if (this.outputConstraints.containsKey(trId))
                trOutputConstraints = this.outputConstraints.get(trId);
            trOutputConstraints.add(this.constraintIdentifier);
            this.outputConstraints.put(trId, trOutputConstraints);
        }
        this.constraintIdentifier++;
    }

    /**
     * Replay the transition on the local process model
     *
     * @param transition: the id of the transition that we want to replay
     * @return true if the transition was successfully replayed, false otherwise
     */
    public boolean fire(int transition) {
        if (!canFire(transition))
            return false;
        // take tokens from all input constraints
        if (this.inputConstraints.containsKey(transition)) {
            for (Integer inputConstraint : this.inputConstraints.get(transition)) {
                Constraint constraint = this.constraintMap.get(inputConstraint);
                constraint.takeTokens();
                this.usedConstraints.add(inputConstraint); // constrained has been used
            }
        }
        // add tokens to all output constraints
        if (this.outputConstraints.containsKey(transition)) {
            for (Integer outputConstraint : this.outputConstraints.get(transition)) {
                Constraint constraint = this.constraintMap.get(outputConstraint);
                constraint.addTokens();
            }
        }
        if (!invisibleTransitions.contains(transition)) { // if a visible transition has fired
            this.invisibleFired.clear(); // clear the invisible transitions that have fired till now
            this.silentFiresCount = 0;
            this.hasFired = true;
        } else {
            this.invisibleFired.add(transition);
            this.silentFiresCount++;
        }
        this.firingSequence.add(transition);
        this.usedPassages.addAll(getUsedPassages(transition));

        return true;
    }

    /**
     * Calculate the used passages after firing the latest transition
     * @param transition: the fired transition
     * @return the set of used passages
     */
    private Set<Pair<Integer, Integer>> getUsedPassages(int transition) {
        Set<Integer> inConstraints = this.inputConstraints.get(transition);
        if (inConstraints == null || inConstraints.isEmpty()) {
            return new HashSet<>();
        }

        Set<Integer> inTransitions =
                this.outputConstraints.entrySet()
                        .stream()
                        .filter(e -> !Sets.intersection(inConstraints, e.getValue()).isEmpty())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
        return inTransitions.stream().map(t -> new Pair<>(t, transition)).collect(Collectors.toSet());
    }

    /**
     * Checks whether the transition can be fired
     *
     * @param transition: the id of the transition we want to fire
     * @return true if the transition can be fired, false otherwise
     */
    public boolean canFire(int transition) {
        if (!transitions.contains(transition) || this.invisibleFired.contains(transition))
            return false;

        // check if tokens can be taken from all input constraints
        Set<Integer> inputConstraints = this.inputConstraints.get(transition);
        if (inputConstraints != null) {
            for (Integer inputConstraint : inputConstraints) {
                Constraint constraint = this.constraintMap.get(inputConstraint);
                if (constraint.canNotTake())
                    return false;
            }
        }
        // check if tokens can be added to all output constraints
        Set<Integer> outputConstraints = this.outputConstraints.get(transition);
        if (outputConstraints != null) {
            for (Integer outputConstraint : outputConstraints) {
                Constraint constraint = this.constraintMap.get(outputConstraint);
                if ((inputConstraints == null || !inputConstraints.contains(outputConstraint)) &&
                        constraint.canNotAdd())
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks whether all constraints in the local process model are empty
     *
     * @return true if all are empty and false otherwise
     */
    public boolean isEmptyMarking() {
        for (Constraint constraint : constraintMap.values()) {
            if (constraint.getNumTokens() != 0)
                return false;
        }
        return true;
    }

    public Set<Integer> getEnabledTransitions() {
        Set<Integer> enabledTransitions = new HashSet<>();
        for (Integer tr : this.transitions) {
            if (this.canFire(tr))
                enabledTransitions.add(tr);
        }
        return enabledTransitions;
    }

    public Set<Integer> getEnabledInvisible() {
        Set<Integer> invisibleEnabled = new HashSet<>();
        for (Integer invisibleTr : this.invisibleTransitions) {
            if (this.canFire(invisibleTr))
                invisibleEnabled.add(invisibleTr);
        }
        return invisibleEnabled;
    }

    public boolean hasInputConstraint(Integer transition) {
        return !this.inputConstraints.get(transition).isEmpty();
    }

    public boolean hasFired() {
        return this.hasFired;
    }

    public int getSilentFiresCount() {
        return silentFiresCount;
    }

    @Override
    public String toString() {
        return "ReplayableLocalProcessModel{" +
                "constraintMap=" + constraintMap +
                ", inputConstraints=" + inputConstraints +
                ", outputConstraints=" + outputConstraints +
                ", firingSequence=" + firingSequence +
                '}';
    }
}

class Constraint {
    private int numTokens;
    private final int maxTokens;
    private final String id;

    Constraint(String id, int numTokens) {
        this.numTokens = numTokens;
        this.maxTokens = 1;
        this.id = id;
    }

    public Constraint(Constraint constraint) {
        this.numTokens = constraint.numTokens;
        this.maxTokens = constraint.maxTokens;
        this.id = constraint.id;
    }

    int getNumTokens() {
        return numTokens;
    }

    public String getId() {
        return id;
    }

    void takeTokens() {
        if (this.canNotTake())
            return;
        this.numTokens -= 1;
    }

    void addTokens() {
        if (this.canNotAdd())
            return;
        this.numTokens += 1;
    }

    boolean canNotTake() {
        return this.numTokens < 1;
    }

    boolean canNotAdd() {
        return this.numTokens + 1 > this.maxTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return numTokens == that.numTokens &&
                maxTokens == that.maxTokens;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numTokens, maxTokens);
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "id=" + id +
                "numTokens=" + numTokens +
                ", maxTokens=" + maxTokens +
                '}';
    }
}
