package org.processmining.placebasedlpmdiscovery;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.deckfour.xes.model.XLog;

public class InputModule extends AbstractModule {

    private final XLog log;

    public InputModule(XLog log) {
        this.log = log;
    }

    @Provides
    XLog provideAnalyzedLog() {
        return this.log;
    }

    @Override
    protected void configure() {}
}
