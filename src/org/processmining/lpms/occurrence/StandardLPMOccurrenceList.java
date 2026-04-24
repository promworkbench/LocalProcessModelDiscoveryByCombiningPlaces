package org.processmining.lpms.occurrence;

import org.apache.commons.math3.util.Pair;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class StandardLPMOccurrenceList implements LPMOccurrenceList {

    TreeMap<Integer, TreeSet<Integer>> coveredEvents;

    public StandardLPMOccurrenceList() {
        coveredEvents = new TreeMap<>();
    }

    @Override
    public void push(int traceIndex, int eventIndex) {
        coveredEvents.computeIfAbsent(traceIndex, k -> new TreeSet<>()).add(eventIndex);
    }

    @Override
    public int size() {
        return coveredEvents.values().stream().mapToInt(TreeSet::size).sum();
    }

    @Override
    public Iterator<Pair<Integer, Integer>> iterator() {
        return coveredEvents.entrySet().stream().flatMap(e ->
                e.getValue().stream().map(v -> Pair.create(e.getKey(), v))).iterator();
    }
}
