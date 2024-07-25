package org.processmining.placebasedlpmdiscovery.prom.datacommunication;

import com.google.inject.Inject;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.export.ExportRequestedEmittableDataVM;
import org.processmining.plugins.utils.ProvidedObjectHelper;

public class GlobalDCListenerForProm implements DataListenerVM {

    private final PluginContext context;

    @Inject
    public GlobalDCListenerForProm(PluginContext context, DataCommunicationControllerVM dcVM) {
        this.context = context;
        dcVM.registerDataListener(this, EmittableDataTypeVM.ExportRequestedVM.name());
    }

    @Override
    public void receive(EmittableDataVM data) {
        if (data.getType().equals(EmittableDataTypeVM.ExportRequestedVM)) {
            ExportRequestedEmittableDataVM cData = (ExportRequestedEmittableDataVM) data;
            Object exportData = cData.getData();
            if (exportData instanceof LPMResult) {
                LPMResult lpmResult = (LPMResult) exportData;
                context.getProvidedObjectManager()
                        .createProvidedObject("Collection exported from LPM Discovery plugin", lpmResult, LPMResult.class, context);
                ProvidedObjectHelper.setFavorite(context, lpmResult);
            }

            if (exportData instanceof PlaceSet) {
                PlaceSet places = (PlaceSet) exportData;
                context.getProvidedObjectManager()
                        .createProvidedObject("Collection exported from LPM Discovery plugin", places, PlaceSet.class, context);
                ProvidedObjectHelper.setFavorite(context, places);
            }
        }
    }
}
