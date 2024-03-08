package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.serialization;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.*;
import org.python.google.common.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public class AttributeSummaryAdapter implements JsonSerializer<AttributeSummary<?, ?>>,
        JsonDeserializer<AttributeSummary<?, ?>> {
    @Override
    public AttributeSummary<?, ?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        AttributeSummary<?, ?> attributeSummary = null;
        switch (jsonObject.get("attributeClass").getAsString()) {
            case "XAttributeContinuousImpl":
                attributeSummary = new ContinuousAttributeSummary(
                        jsonObject.get("key").getAsString(),
                        jsonObject.get("completeList").getAsBoolean());
                break;
            case "XAttributeDiscreteImpl":
                attributeSummary = new DiscreteAttributeSummary(
                        jsonObject.get("key").getAsString(),
                        jsonObject.get("completeList").getAsBoolean());
                break;
            case "XAttributeLiteralImpl":
                attributeSummary = new LiteralAttributeSummary(
                        jsonObject.get("key").getAsString(),
                        jsonObject.get("completeList").getAsBoolean());
                break;
            case "XAttributeTimestampImpl":
                attributeSummary = new TimestampAttributeSummary(
                        jsonObject.get("key").getAsString(),
                        jsonObject.get("completeList").getAsBoolean());
                break;
            case "XAttributeBooleanImpl":
                attributeSummary = new BooleanAttributeSummary(
                        jsonObject.get("key").getAsString(),
                        jsonObject.get("completeList").getAsBoolean());
                break;
        }

        try {
            Field f = AttributeSummary.class.getDeclaredField("representationFeatures");
            f.setAccessible(true);
            f.set(attributeSummary, context.deserialize(jsonObject.get("representationFeatures"),
                    new TypeToken<Map<String, Number>>(){}.getType()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return attributeSummary;
    }

    @Override
    public JsonElement serialize(AttributeSummary<?, ?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("representationFeatures", context.serialize(src.getRepresentationFeatures()));
        jsonObject.addProperty("key", src.getKey());
        jsonObject.addProperty("completeList", false);
        jsonObject.addProperty("attributeClass", src.getAttributeClass().getSimpleName());
        return jsonObject;
    }
}
