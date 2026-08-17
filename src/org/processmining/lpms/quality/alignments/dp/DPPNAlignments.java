package org.processmining.lpms.quality.alignments.dp;

import nl.tue.astar.AStarException;
import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.lpms.quality.alignments.dp.dependency.*;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.AllSyncReplayResult;

import java.util.*;
import java.util.stream.Collectors;

public class DPPNAlignments implements PNAlignments {

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XLog log) throws AStarException {
        PNMatchInstancesRepResult results = new PNMatchInstancesRepResult(new ArrayList<>());
        int traceIndex = 0;
        for (XTrace trace : log) {
            results.addAll(computeSingleTrace(apn, trace, traceIndex));
            traceIndex++;
        }
        return results;
    }

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XTrace trace) throws AStarException {
        return computeSingleTrace(apn, trace, 0);
    }

    private PNMatchInstancesRepResult computeSingleTrace(AcceptingPetriNet apn, XTrace trace, int traceIndex) throws AStarException {
        // index to event activity
        ArrayList<String> eventActivities = trace.stream().map(e -> e.getAttributes().get("concept:name").toString())
                .collect(Collectors.toCollection(ArrayList::new));

        // all transitions
        ArrayList<Transition> transitions = new ArrayList<>(apn.getNet().getTransitions());
        // transition to index
        Map<Transition, Integer> transitionToIndex = transitions.stream()
                .collect(HashMap::new, (m, t) -> m.put(t, transitions.indexOf(t)), HashMap::putAll);
        // index to transition
        Map<Integer, Transition> indexToTransition =
                transitionToIndex.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        // transition index to dependency
        Map<Integer, DependencyExpression> transitionDependencyMap = new HashMap<>();
        for (Map.Entry<Transition, DependencyExpression> entry : DirectDependencyComputer.compute(apn).entrySet()) {
            transitionDependencyMap.put(transitionToIndex.get(entry.getKey()), DnfConverter.toDnf(entry.getValue()));
        }

        // matrix containing the results from the dynamic programming, rows denote transitions and columns events
        int[][] dp = new int[apn.getNet().getTransitions().size()][trace.size()];
        List<DPAlignmentResult> alignmentsPerTransitionLast = new ArrayList<>();
        List<DPAlignmentResult> alignmentsPerTransitionCurrent = new ArrayList<>();

        // initialize first column
        for (int i = 0; i < apn.getNet().getTransitions().size(); i++) {
            DPAlignmentResult alignmentResult = new DPAlignmentResult(1, false);
            if (eventActivities.get(0).equals(indexToTransition.get(i).getLabel()) &&
                    transitionDependencyMap.get(i).equals(SingleExecutionDependency.INSTANCE)) {
                dp[i][0] = 1; // one match of the first log event until here
                alignmentResult.insert(DPAlignmentResult.SYNC_MOVE); // push a sync move
            } else {
                dp[i][0] = 0; // no matches to the first log event at this transition
                alignmentResult.insert(DPAlignmentResult.LOG_MOVE); // push a log move
            }
            alignmentsPerTransitionCurrent.add(alignmentResult);
        }

        // populate the rest of the matrix, all rows (transitions) for a single column (event) before continuing to the
        // next row (event)
        for (int j = 1; j < trace.size(); j++) {
            alignmentsPerTransitionLast = alignmentsPerTransitionCurrent;
            alignmentsPerTransitionCurrent = new ArrayList<>();

            for (int i = 0; i < apn.getNet().getTransitions().size(); i++) {
                if (!indexToTransition.get(i).getLabel().equals(eventActivities.get(j))) {
                    // if no matching labels, the number of occurrences stays the same as until the previous event
                    dp[i][j] = dp[i][j - 1];
                    // push log move to all alignments from previous step
                    alignmentsPerTransitionCurrent.add(alignmentsPerTransitionLast.get(i).copyOneBigger());
                    alignmentsPerTransitionCurrent.get(i).insert(DPAlignmentResult.LOG_MOVE);
                } else { // if matching label compute the count based on the dependencies
                    Pair<Integer, DPAlignmentResult> resultPair = computeCountOfNewOccurrences(dp,
                            transitionDependencyMap,
                            transitionDependencyMap.get(i), i, j, indexToTransition, transitionToIndex,
                            alignmentsPerTransitionLast);
                    dp[i][j] = dp[i][j - 1] + resultPair.getFirst();
                    alignmentsPerTransitionCurrent.add(resultPair.getSecond().copyOneBigger());
                    alignmentsPerTransitionCurrent.get(i).insert(DPAlignmentResult.SYNC_MOVE);
                    if (dp[i][j - 1] > 0) { // add all alignments computed for this transition until the previous event
                        // and extend them with a log move for the current event
                        DPAlignmentResult withLogMove = alignmentsPerTransitionLast.get(i).copyOneBigger();
                        withLogMove.insert(DPAlignmentResult.LOG_MOVE);
                        alignmentsPerTransitionCurrent.get(i).addAll(withLogMove);
                    }
                }
            }
        }

        DependencyExpression endDependency = computeEndDependency(apn, transitionToIndex);
        Pair<Integer, DPAlignmentResult> finalResult = computeCountOfNewOccurrences(dp, transitionDependencyMap,
                endDependency, trace.size(), indexToTransition, transitionToIndex, alignmentsPerTransitionCurrent);
        return convertResult(finalResult.getSecond(), traceIndex);
    }

    private DependencyExpression computeEndDependency(AcceptingPetriNet apn,
                                                      Map<Transition, Integer> transitionToIndex) {
        List<DependencyExpression> orChildren = new ArrayList<>();
        for (Marking marking : apn.getFinalMarkings()) {
            orChildren.add(DirectDependencyComputer.computeForMarking(apn, marking));
        }

        orChildren = orChildren.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (orChildren.isEmpty()) {
            List<Transition> emptyPostsetTransitions = computeEmptyPostsetTransitions(apn.getNet());
            if (emptyPostsetTransitions.isEmpty()) {
                throw new IllegalStateException("No final marking nor empty postset transitions.");
            }
            return emptyPostsetTransitions.size() == 1 ? new ActivityDependency(emptyPostsetTransitions.get(0)) :
                    new OrDependency(emptyPostsetTransitions.stream().map(ActivityDependency::new).collect(Collectors.toList()));
        }
        return orChildren.size() == 1 ? orChildren.get(0) : new OrDependency(orChildren);
    }

    private List<Transition> computeEmptyPostsetTransitions(Petrinet net) {
        return net.getTransitions().stream()
                .filter(t -> net.getOutEdges(t).isEmpty())
                .collect(Collectors.toList());
    }

    private PNMatchInstancesRepResult convertResult(DPAlignmentResult dpAlignmentResult, int traceIndex) {
        List<List<Object>> nodeInstanceLst = new ArrayList<>();
        List<List<StepTypes>> stepTypesLst = new ArrayList<>();
        for (Integer[] alignment : dpAlignmentResult.getAlignments()) {
            List<StepTypes> stepTypes = new ArrayList<>(alignment.length);
            for (int move : alignment) {
                stepTypes.add(move == DPAlignmentResult.SYNC_MOVE ? StepTypes.LMGOOD : StepTypes.L);
            }
            stepTypesLst.add(stepTypes);
            nodeInstanceLst.add(Collections.nCopies(alignment.length, null));
        }

        AllSyncReplayResult replayResult = new AllSyncReplayResult(nodeInstanceLst, stepTypesLst, traceIndex, true);
        return new PNMatchInstancesRepResult(Collections.singletonList(replayResult));
    }

    private Pair<Integer, DPAlignmentResult> computeCountOfNewOccurrences(int[][] dp,
                                                                          Map<Integer, DependencyExpression> transitionDependencyMap,
                                                                          DependencyExpression dependencyExpression,
                                                                          int row,
                                                                          int column,
                                                                          Map<Integer, Transition> indexToTransition,
                                                                          Map<Transition, Integer> transitionToIndex,
                                                                          List<DPAlignmentResult> alignmentsPerTransitionLast) {
        if (dependencyExpression.equals(SingleExecutionDependency.INSTANCE)) {
            return new Pair<>(1, alignmentsPerTransitionLast.get(row));
        }
        if (dependencyExpression instanceof ActivityDependency) {
            int depIndex = transitionToIndex.get(((ActivityDependency) dependencyExpression).getTransition());
            return new Pair<>(dp[depIndex][column - 1], alignmentsPerTransitionLast.get(depIndex));
        }
        if (dependencyExpression instanceof OrDependency) {
            int sum = 0;
            DPAlignmentResult collectedAlignments =
                    new DPAlignmentResult(alignmentsPerTransitionLast.get(0).alignmentLength());
            for (DependencyExpression disjunt : ((OrDependency) dependencyExpression).getChildren()) {
                Pair<Integer, DPAlignmentResult> resultPair = computeCountOfNewOccurrences(dp,
                        transitionDependencyMap, disjunt, row, column, indexToTransition,
                        transitionToIndex, alignmentsPerTransitionLast);
                sum += resultPair.getFirst();
                collectedAlignments.addAll(resultPair.getSecond());
            }
            return new Pair<>(sum, collectedAlignments);
        }
        if (dependencyExpression instanceof AndDependency) {
            List<Transition> dependentTransitions = ((AndDependency) dependencyExpression).getChildren().stream()
                    .map(child -> ((ActivityDependency) child).getTransition())
                    .collect(Collectors.toList());

            Map<Transition, DependencyExpression> commonDependencies = transitionDependencyMap.entrySet().stream()
                    .filter(entry -> dependentTransitions.contains(indexToTransition.get(entry.getKey())))
                    .collect(Collectors.toMap(entry -> indexToTransition.get(entry.getKey()), Map.Entry::getValue));

            Map<Transition, Set<Transition>> sharedDependencies =
                    SharedDependencyFinder.sharedDependencies(dependentTransitions,
                            commonDependencies);

            double product = 1;
            DPAlignmentResult crossProd = DPAlignmentResult.createAllLogMoves(
                    1, alignmentsPerTransitionLast.get(0).alignmentLength());
            Set<Transition> covered = new HashSet<>();
            for (Transition transition : dependentTransitions) {
                Set<DPAlignmentResult> mustShare = sharedDependencies.entrySet().stream()
                        .filter(e -> e.getValue().contains(transition)
                                && e.getValue().stream().anyMatch(covered::contains))
                        .map(Map.Entry::getKey)
                        .map(t -> alignmentsPerTransitionLast.get(transitionToIndex.get(t)))
                        .collect(Collectors.toSet());

                int depIndex = transitionToIndex.get(transition);
                crossProd = DPAlignmentResult.crossProduct(crossProd, alignmentsPerTransitionLast.get(depIndex),
                        mustShare);
                product *= dp[transitionToIndex.get(transition)][column - 1];

                covered.add(transition); // the alignments for this transition have been covered
            }

            for (Map.Entry<Transition, Set<Transition>> entry : sharedDependencies.entrySet()) {
                product /= Math.pow(dp[transitionToIndex.get(entry.getKey())][column - 1], entry.getValue().size() - 1);
            }
            int parsedProduct = (int) Math.rint(product);
            if (Math.abs(product - parsedProduct) > 1e-9) {
                throw new IllegalStateException("Product of counts is not an integer: " + product);
            }
            if (crossProd.getAlignments().size() != parsedProduct) {
                throw new IllegalStateException("The number of created alignments should match the computed product.");
            }
            return new Pair<>(parsedProduct, crossProd);
        }
        throw new IllegalArgumentException("Unknown dependency expression type: " + dependencyExpression.getClass().getName());
    }

    private Pair<Integer, DPAlignmentResult> computeCountOfNewOccurrences(int[][] dp,
                                                                          Map<Integer, DependencyExpression> transitionDependencyMap,
                                                                          DependencyExpression dependencyExpression,
                                                                          int column,
                                                                          Map<Integer, Transition> indexToTransition,
                                                                          Map<Transition, Integer> transitionToIndex,
                                                                          List<DPAlignmentResult> alignmentsPerTransitionLast) {
        if (dependencyExpression instanceof ActivityDependency) {
            int depIndex = transitionToIndex.get(((ActivityDependency) dependencyExpression).getTransition());
            return new Pair<>(dp[depIndex][column - 1], alignmentsPerTransitionLast.get(depIndex));
        }
        if (dependencyExpression instanceof OrDependency) {
            int sum = 0;
            DPAlignmentResult collectedAlignments =
                    new DPAlignmentResult(alignmentsPerTransitionLast.get(0).alignmentLength());
            for (DependencyExpression disjunt : ((OrDependency) dependencyExpression).getChildren()) {
                Pair<Integer, DPAlignmentResult> resultPair = computeCountOfNewOccurrences(dp,
                        transitionDependencyMap, disjunt, column, indexToTransition,
                        transitionToIndex, alignmentsPerTransitionLast);
                sum += resultPair.getFirst();
                collectedAlignments.addAll(resultPair.getSecond());
            }
            return new Pair<>(sum, collectedAlignments);
        }
        if (dependencyExpression instanceof AndDependency) {
            List<Transition> dependentTransitions = ((AndDependency) dependencyExpression).getChildren().stream()
                    .map(child -> ((ActivityDependency) child).getTransition())
                    .collect(Collectors.toList());

            Map<Transition, DependencyExpression> commonDependencies = transitionDependencyMap.entrySet().stream()
                    .filter(entry -> dependentTransitions.contains(indexToTransition.get(entry.getKey())))
                    .collect(Collectors.toMap(entry -> indexToTransition.get(entry.getKey()), Map.Entry::getValue));

            Map<Transition, Set<Transition>> sharedDependencies =
                    SharedDependencyFinder.sharedDependencies(dependentTransitions,
                            commonDependencies);

            double product = 1;
            DPAlignmentResult crossProd = DPAlignmentResult.createAllLogMoves(
                    1, alignmentsPerTransitionLast.get(0).alignmentLength());
            Set<Transition> covered = new HashSet<>();
            for (Transition transition : dependentTransitions) {
                Set<DPAlignmentResult> mustShare = sharedDependencies.entrySet().stream()
                        .filter(e -> e.getValue().contains(transition)
                                && e.getValue().stream().anyMatch(covered::contains))
                        .map(Map.Entry::getKey)
                        .map(t -> alignmentsPerTransitionLast.get(transitionToIndex.get(t)))
                        .collect(Collectors.toSet());

                int depIndex = transitionToIndex.get(transition);
                crossProd = DPAlignmentResult.crossProduct(crossProd, alignmentsPerTransitionLast.get(depIndex),
                        mustShare);
                product *= dp[transitionToIndex.get(transition)][column - 1];

                covered.add(transition); // the alignments for this transition have been covered
            }

            for (Map.Entry<Transition, Set<Transition>> entry : sharedDependencies.entrySet()) {
                product /= Math.pow(dp[transitionToIndex.get(entry.getKey())][column - 1], entry.getValue().size() - 1);
            }
            int parsedProduct = (int) Math.rint(product);
            if (Math.abs(product - parsedProduct) > 1e-9) {
                throw new IllegalStateException("Product of counts is not an integer: " + product);
            }
            if (crossProd.getAlignments().size() != parsedProduct) {
                throw new IllegalStateException("The number of created alignments should match the computed product.");
            }
            return new Pair<>(parsedProduct, crossProd);
        }
        throw new IllegalArgumentException("Unknown dependency expression type: " + dependencyExpression.getClass().getName());
    }

//    private DPAlignmentResult reconstructAlignmentsFromDp(int[][] dp, int row, int col, DPAlignmentResult result) {
//        // if we iterated through the entire matrix return the result
//        if (row < 0 || col < 0) {
//            return result;
//        }
//        // if the current cell is not greater than the one in the left nothing changed for that event so push a log
//        // move and continue reconstructing from that position
//        if (col > 0 && dp[row][col] == dp[row][col - 1]) {
//            result.insert(DPAlignmentResult.LOG_MOVE);
//            return reconstructAlignmentsFromDp(dp, row, col - 1, result);
//        }
//        // left is smaller than me -> new occurrences were noted
//
//
//        // there is no left
//    }

}