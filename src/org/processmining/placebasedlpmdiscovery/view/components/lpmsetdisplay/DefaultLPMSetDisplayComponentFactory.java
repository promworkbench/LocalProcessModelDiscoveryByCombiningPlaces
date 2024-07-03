package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultLPMSetDisplayComponentFactory implements LPMSetDisplayComponentFactory {

    private final PluginVisualizerTableFactory<LocalProcessModel> tableFactory;
    private final DataCommunicationControllerVM dcVM;

    @Inject
    public DefaultLPMSetDisplayComponentFactory(PluginVisualizerTableFactory<LocalProcessModel> tableFactory,
                                                DataCommunicationControllerVM dcVM) {
        this.tableFactory = tableFactory;
        this.dcVM = dcVM;
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
            LPMSetDisplayComponent component = new SimpleCollectionOfElementsComponent<>(lpms,
                    tableFactory, listener, dcVM);
            return component;
        }
        throw new IllegalArgumentException("No implementation for type " + type.name());
    }
}
