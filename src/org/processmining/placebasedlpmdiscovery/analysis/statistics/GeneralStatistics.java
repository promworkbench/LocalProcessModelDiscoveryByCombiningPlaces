package org.processmining.placebasedlpmdiscovery.analysis.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class GeneralStatistics implements IStatistics {


    private final UUID executionId;

    private String eventLogName;
    private int countPlacesUsed;
    private int proximity;
    private int lpmDiscovered;
    private int lpmReturned;
    private boolean interrupted;
    private Duration lpmDiscoveryDuration;
    private Duration totalDuration;

    public GeneralStatistics(UUID executionId) {
        this.executionId = executionId;
    }

    @Override
    public void write(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tLog Name\tTotal Execution Time\tLPM Discovery Execution Time\t" +
                        "Interrupted\tCount Places Used\tProximity\tLPM discovered\tLPM returned\tTimestamp");

            pw.println(String.join("\t", String.valueOf(this.executionId), this.eventLogName,
                    String.valueOf(this.totalDuration),
                    String.valueOf(this.lpmDiscoveryDuration),
                    String.valueOf(this.interrupted),
                    String.valueOf(this.countPlacesUsed),
                    String.valueOf(this.proximity),
                    String.valueOf(this.lpmDiscovered),
                    String.valueOf(this.lpmReturned),
                    String.valueOf(Date.from(Instant.now()))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setEventLogName(String eventLogName) {
        this.eventLogName = eventLogName;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setCountPlacesUsed(int countPlacesUsed) {
        this.countPlacesUsed = countPlacesUsed;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public void setLpmDiscovered(int lpmDiscovered) {
        this.lpmDiscovered = lpmDiscovered;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public void setLpmReturned(int lpmReturned) {
        this.lpmReturned = lpmReturned;
    }

    public void setLpmDiscoveryDuration(Duration lpmDiscoveryDuration) {
        this.lpmDiscoveryDuration = lpmDiscoveryDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }
}
