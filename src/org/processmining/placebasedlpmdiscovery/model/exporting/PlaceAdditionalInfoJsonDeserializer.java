package org.processmining.placebasedlpmdiscovery.model.exporting;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.Passage;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.PlaceAdditionalInfo;
import org.python.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class PlaceAdditionalInfoJsonDeserializer implements JsonDeserializer<PlaceAdditionalInfo> {
    @Override
    public PlaceAdditionalInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        PlaceAdditionalInfo pai = new PlaceAdditionalInfo();
        pai.setPassageUsage(context.deserialize(jsonObject.get("passageUsage"), new TypeToken<Map<Passage, Integer>>(){}.getType()));
        return pai;
    }
}
