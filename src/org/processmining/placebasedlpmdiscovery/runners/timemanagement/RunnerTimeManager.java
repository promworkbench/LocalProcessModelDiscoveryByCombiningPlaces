package org.processmining.placebasedlpmdiscovery.runners.timemanagement;

import org.apache.commons.math3.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RunnerTimeManager {

    private final Map<String, Pair<Long, Long>> timers;

    public RunnerTimeManager() {
        this.timers = new java.util.HashMap<>();
    }

    public void startTimer(String key) {
        if (timers.containsKey(key)) {
            throw new IllegalArgumentException("Timer with key " + key + " already exists");
        }
        timers.put(key, Pair.create(System.nanoTime(), Long.MIN_VALUE));
    }

    public void stopTimer(String key) {
        if (!timers.containsKey(key)) {
            throw new IllegalArgumentException("Timer with key " + key + " does not exist");
        }
        Pair<Long, Long> timer = timers.get(key);
        timers.put(key, Pair.create(timer.getFirst(), System.nanoTime()));
    }

    public void exportTimers(String outputFileName) {
        try (FileWriter writer = new FileWriter(outputFileName)) {
            writer.write("Timer,Duration(ms)\n");
            for (Map.Entry<String, Pair<Long, Long>> entry : timers.entrySet()) {
                writer.write(String.format("%s,%d\n",
                        entry.getKey(),
                        TimeUnit.NANOSECONDS.toMillis(
                                entry.getValue().getSecond() - entry.getValue().getFirst())));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing timers to file: " + outputFileName, e);
        }
    }

}
