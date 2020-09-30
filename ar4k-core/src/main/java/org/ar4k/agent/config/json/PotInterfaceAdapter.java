package org.ar4k.agent.config.json;

import java.lang.reflect.Type;

import org.ar4k.agent.core.interfaces.ServiceConfig;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@SuppressWarnings("rawtypes")
/**
 * adattatore utilizzato da Gson per la conversione delle classi implementate
 * nei pots della configurazione di Anima
 *
 * @see org.ar4k.agent.config.Ar4kConfig
 *
 * @author andrea
 *
 */
public class PotInterfaceAdapter implements JsonSerializer, JsonDeserializer {

  private static final String CLASSNAME = "class-name";
  private static final String DATA = "data";

  @Override
  public ServiceConfig deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
    String className = prim.getAsString();
    Class klass = getObjectClass(className);
    return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
  }

  @Override
  public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
    jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
    return jsonObject;
  }

  public Class getObjectClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      throw new JsonParseException(e.getMessage());
    }
  }

}
