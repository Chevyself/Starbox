package me.googas.adapters.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import me.googas.starbox.math.geometry.Box;
import me.googas.starbox.math.geometry.Cylinder;
import me.googas.starbox.math.geometry.Shape;
import me.googas.starbox.math.geometry.SinglePoint;
import me.googas.starbox.math.geometry.Sphere;

/** Deserializes {@link Shape} from json. */
public class ShapeDeserializer implements JsonDeserializer<Shape> {

  @Override
  public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject object = json.getAsJsonObject();
    if (object.get("minimum") != null && object.get("maximum") != null) {
      return context.deserialize(object, Box.class);
    } else if (object.get("base") != null
        && object.get("radius") != null
        && object.get("height") != null) {
      return context.deserialize(object, Cylinder.class);
    } else if (object.get("center") != null && object.get("radius") != null) {
      return context.deserialize(object, Sphere.class);
    } else if (object.get("point") != null) {
      return context.deserialize(object, SinglePoint.class);
    } else {
      throw new JsonParseException("Your input does not match a known shape in: " + object);
    }
  }
}
