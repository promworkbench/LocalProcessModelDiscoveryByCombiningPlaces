package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.gson.adapters.GeneralInterfaceAdapter;
import org.processmining.placebasedlpmdiscovery.model.exporting.gson.adapters.LPMDiscoveryResultAdapter;
import org.processmining.placebasedlpmdiscovery.model.logs.Event;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityId;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTrace;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariant;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.serialization.AttributeSummaryAdapter;
import org.python.google.common.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class JsonExporter<T> implements Exporter<T> {

    @Override
    public void export(T object, OutputStream os) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(LPMDiscoveryResult.class, new LPMDiscoveryResultAdapter())
                .registerTypeAdapter(AttributeSummary.class, new AttributeSummaryAdapter())
                .registerTypeAdapter(ActivityId.class, new GeneralInterfaceAdapter<ActivityId>())
                .registerTypeAdapter(Activity.class, new GeneralInterfaceAdapter<Activity>())
                .registerTypeAdapter(Event.class, new GeneralInterfaceAdapter<Event>())
                .registerTypeAdapter(EventLogTrace.class, new GeneralInterfaceAdapter<EventLogTrace<?>>())
                .registerTypeAdapter(EventLogTraceVariant.class, new GeneralInterfaceAdapter<EventLogTraceVariant<?>>())
                .create();
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(os))) {
            gson.toJson(object, new TypeToken<LPMDiscoveryResult>(){}.getType(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
