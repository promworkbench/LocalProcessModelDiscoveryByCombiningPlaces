package org.processmining.lpms.io.readers;

import org.processmining.lpms.model.LPM;

import java.util.Collection;

public interface LPMsReader {

    static LPMsReader zip(String fileName) {
        return new LPMsFromPNMLsInZipReader(fileName);
    }

    Collection<LPM> getLPMs();
}
