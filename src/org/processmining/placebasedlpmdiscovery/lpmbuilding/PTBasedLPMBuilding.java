package org.processmining.placebasedlpmdiscovery.lpmbuilding;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.LPMStorage;

public class PTBasedLPMBuilding implements LPMBuilding {
    private final int proximity;

    public PTBasedLPMBuilding(int proximity) {
        this.proximity = proximity;
    }

    @Override
    public LPMStorage from(XLog log) {
        throw new NotImplementedException();
//        EventLog eventLog = new XLogWrapper(log);
//
//        GlobalLPMStorage lpmStorage = GlobalLPMStorage.getInstance();
//        WindowToGlobalLPMStorageTransporter storageTransporter = WindowToGlobalLPMStorageTransporter.getInstance();
//
//        // traverse event log and build lpms
//        EventLogWindowTraversal traversal = EventLogWindowTraversal
//                .getInstance(eventLog, this.proximity);
//        WindowLPMStorage windowStorage = null;
//        while (traversal.hasNext()) {
//            IWindowInfo windowInfo = traversal.next();
//            windowStorage = singleWindowLPMBuilder.build(windowInfo, windowStorage);
//            storageTransporter.move(windowStorage, lpmStorage);
//        }
//
//        return new DefaultLPMBuildingResult(lpmStorage);
    }
}
