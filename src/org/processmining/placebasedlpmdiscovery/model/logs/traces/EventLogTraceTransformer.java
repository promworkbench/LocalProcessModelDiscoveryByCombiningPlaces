package org.processmining.placebasedlpmdiscovery.model.logs.traces;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XTrace;

public interface EventLogTraceTransformer {
    static XTrace toXTrace(EventLogTrace<?> trace) {
        if (trace instanceof XTraceWrapper) {
            XTraceWrapper cTrace = (XTraceWrapper) trace;
            return cTrace.getOriginalTrace();
        }
        throw new NotImplementedException("Not yet implemented for " + trace.getClass());
    }
}
