package org.processmining.placebasedlpmdiscovery.view.components;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultComponentFactory implements ComponentFactory {

    private final LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory;

    @Inject
    public DefaultComponentFactory(LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory) {
        this.lpmSetDisplayComponentFactory = lpmSetDisplayComponentFactory;
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms);
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               NewElementSelectedListener<LocalProcessModel> listener) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms, listener);
    }
}
