package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.functional;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.AbstractEnhancedLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class CompositeLog {

    Collection<AbstractEnhancedLog> logs;

    public CompositeLog(AbstractEnhancedLog ... logs) {
        this.logs = new ArrayList<>();
        Collections.addAll(this.logs, logs);
    }
}
