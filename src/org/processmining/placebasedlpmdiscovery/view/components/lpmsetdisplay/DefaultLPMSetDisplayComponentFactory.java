package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;
import java.util.Map;

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
                                                               Collection<LocalProcessModel> lpms,
                                                               Map<String, Object> parameters) {
        return createLPMSetDisplayComponent(type, lpms, null, parameters);
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               NewElementSelectedListener<LocalProcessModel> listener,
                                                               Map<String, Object> parameters) {
        if (type.equals(LPMSetDisplayComponentType.SimpleLPMsCollection)) {
            return new SimpleCollectionOfElementsComponent<>(lpms, tableFactory, listener, dcVM);
        } else if (type.equals(LPMSetDisplayComponentType.GroupedLPMs)) {
            return new GroupedLPMsComponent(lpms, (String) parameters.get("identifier"), this);
        }
        throw new IllegalArgumentException("No implementation for type " + type.name());
    }
}
