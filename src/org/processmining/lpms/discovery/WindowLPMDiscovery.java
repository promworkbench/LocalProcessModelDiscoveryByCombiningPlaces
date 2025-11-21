package org.processmining.lpms.discovery;

import org.deckfour.xes.model.XLog;
import org.processmining.eventlogs.window.WindowBasedEventLog;
import org.processmining.lpms.discovery.builders.LADAWindowLPMBuilder;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowToGlobalLPMStorageTransporter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

/**
 * LPM discovery implementation that builds LPMs using a sliding window approach.
 * For each window, LPMs are built using the provided window LPM builder.
 * The LPMs from each window are then transported to a global storage.
 */
public class WindowLPMDiscovery implements LPMDiscovery {

    private final int windowSize;
    private final LADAWindowLPMBuilder windowLPMBuilder;

    public WindowLPMDiscovery() {
        this(DiscoveryParameters.Default.windowSize);
    }

    public WindowLPMDiscovery(int windowSize) {
        this(windowSize, LADAWindowLPMBuilder.getInstance());
    }

    public WindowLPMDiscovery(int windowSize, LADAWindowLPMBuilder windowLPMBuilder) {
        this.windowSize = windowSize;
        this.windowLPMBuilder = windowLPMBuilder;
    }

    /**
     * Build LPMs by traversing the event log with a sliding window and building LPMs for each of them. For each window,
     * LPMs are built using the provided window LPM builder. The LPMs from each window are then transported to a global storage.
     * @param log - the event log to discover LPMs from
     * @return the LPM discovery result containing all discovered LPMs
     */
    @Override
    public LPMDiscoveryResult from(XLog log) {
        EventLog eventLog = new XLogWrapper(log);

        GlobalLPMStorage lpmStorage = GlobalLPMStorage.getInstance();
        WindowToGlobalLPMStorageTransporter storageTransporter = WindowToGlobalLPMStorageTransporter.getInstance();

        // traverse event log and build lpms
        WindowBasedEventLog windowBasedEventLog = WindowBasedEventLog.getInstance(eventLog, this.windowSize);
        WindowLPMStorage windowStorage = null;
        for (SlidingWindowInfo windowInfo : windowBasedEventLog) {
            windowStorage = windowLPMBuilder.build(windowInfo, windowStorage);
            storageTransporter.move(windowStorage, lpmStorage);
        }

        return new StandardLPMDiscoveryResult(lpmStorage.getAllLPMs());
    }
}
