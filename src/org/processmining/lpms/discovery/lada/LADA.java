package org.processmining.lpms.discovery.lada;

import org.deckfour.xes.model.XLog;
import org.processmining.eventlogs.window.WindowBasedEventLog;
import org.processmining.lpms.discovery.DiscoveryParameters;
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

public class LADA implements LPMDiscovery {

    private final int proximity;
    private final LADAWindowLPMBuilder windowLPMBuilder;

    public LADA() {
        this(DiscoveryParameters.Default.proximity);
    }

    public LADA(int proximity) {
        this(proximity, LADAWindowLPMBuilder.getInstance());
    }

    public LADA(int proximity, LADAWindowLPMBuilder windowLPMBuilder) {
        this.proximity = proximity;
        this.windowLPMBuilder = windowLPMBuilder;
    }

    @Override
    public LPMDiscoveryResult from(XLog log) {
        EventLog eventLog = new XLogWrapper(log);

        GlobalLPMStorage lpmStorage = GlobalLPMStorage.getInstance();
        WindowToGlobalLPMStorageTransporter storageTransporter = WindowToGlobalLPMStorageTransporter.getInstance();

        // traverse event log and build lpms
        WindowBasedEventLog windowBasedEventLog = WindowBasedEventLog.getInstance(eventLog, this.proximity);
        WindowLPMStorage windowStorage = null;
        for (SlidingWindowInfo windowInfo : windowBasedEventLog) {
            windowStorage = windowLPMBuilder.build(windowInfo, windowStorage);
            storageTransporter.move(windowStorage, lpmStorage);
        }

        return new StandardLPMDiscoveryResult(lpmStorage.getAllLPMs());
    }
}
