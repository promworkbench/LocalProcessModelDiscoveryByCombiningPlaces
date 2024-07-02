package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultLPMSetDisplayComponentFactory implements LPMSetDisplayComponentFactory {

    private final PluginVisualizerTableFactory<LocalProcessModel> tableFactory;

    @Inject
    public DefaultLPMSetDisplayComponentFactory(PluginVisualizerTableFactory<LocalProcessModel> tableFactory) {
        this.tableFactory = tableFactory;
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms) {
        return createLPMSetDisplayComponent(type, lpms, lpm -> {});
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               NewElementSelectedListener<LocalProcessModel> listener) {
        if (type.equals(LPMSetDisplayComponentType.SimpleLPMsCollection)) {
            LPMSetDisplayComponent component = new SimpleCollectionOfElementsComponent<>(null, lpms,
                    tableFactory, listener);
            return component;
        }
        throw new IllegalArgumentException("No implementation for type " + type.name());
    }
}
