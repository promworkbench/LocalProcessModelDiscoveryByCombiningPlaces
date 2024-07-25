package org.processmining.placebasedlpmdiscovery.service.eventlog;

import com.google.inject.Inject;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Collection;

public class DefaultEventLogService implements EventLogService {

    private final XLog log;

    @Inject
    public DefaultEventLogService(XLog log) {
        this.log = log;
    }
    @Override
    public Collection<String> getEventAttributesKeys() {
        return LogUtils.getEventAttributesKeys(log);
    }
}
