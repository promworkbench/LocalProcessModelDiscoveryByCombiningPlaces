package org.processmining.placebasedlpmdiscovery.model.exporting.gson.adapters;

import com.google.gson.*;
import org.processmining.placebasedlpmdiscovery.model.exporting.gson.GsonSerializable;

import java.lang.reflect.Type;

public class GeneralInterfaceAdapter<INTERFACE extends GsonSerializable> implements JsonSerializer<INTERFACE>,
        JsonDeserializer<INTERFACE> {

    private static final String CLASSNAME = "className";
    private static final String ORIGINAL_OBJECT = "originalObject";


    @Override
    public INTERFACE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        final JsonElement originalObject = jsonObject.get(ORIGINAL_OBJECT);
        final String className = prim.getAsString();
        final Class<INTERFACE> clazz = getClassInstance(className);
        return context.deserialize(originalObject, clazz);
    }

    @Override
    public JsonElement serialize(INTERFACE src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, src.getClassName());
        jsonObject.add(ORIGINAL_OBJECT, jsonElement);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public Class<INTERFACE> getClassInstance(String className) {
        try {
            return (Class<INTERFACE>) Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException(cnfe.getMessage());
        }
    }
}