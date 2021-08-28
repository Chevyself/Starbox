package me.googas.adapters.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import lombok.NonNull;
import me.googas.starbox.math.geometry.Point;

/** Serializes and deserializes {@link Point}. */
public class PointAdapter implements JsonSerializer<Point>, JsonDeserializer<Point> {

  /**
   * Get the json format of an infinite double. If the double is infinite the resulting json
   * primitive will be "-oo" for negative infinite and "oo" for infinite
   *
   * @param number the infinite double to get as json
   * @return the json object
   */
  @NonNull
  public static String infiniteToJson(double number) {
    return number < 0 ? "-oo" : "oo";
  }

  /**
   * Get a double from a json element. This will check if it is a string as that's how {@link
   * #infiniteToJson(double)} save them
   *
   * @param element the json element to get the double from
   * @return the double parsed from the json element
   */
  public static double fromJson(@NonNull JsonElement element) {
    String string = element.getAsString();
    if (string.toLowerCase().contains("oo")) {
      return string.startsWith("-") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    } else {
      return element.getAsDouble();
    }
  }

  @Override
  public JsonElement serialize(
      Point point, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject object = new JsonObject();
    if (Double.isInfinite(point.getX())) {
      object.addProperty("x", PointAdapter.infiniteToJson(point.getX()));
    } else {
      object.addProperty("x", point.getX());
    }
    if (Double.isInfinite(point.getY())) {
      object.addProperty("y", PointAdapter.infiniteToJson(point.getY()));
    } else {
      object.addProperty("y", point.getY());
    }
    if (Double.isInfinite(point.getZ())) {
      object.addProperty("z", PointAdapter.infiniteToJson(point.getZ()));
    } else {
      object.addProperty("z", point.getZ());
    }
    return null;
  }

  @Override
  public Point deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    JsonObject object = jsonElement.getAsJsonObject();
    return new Point(
        PointAdapter.fromJson(object.get("x")),
        PointAdapter.fromJson(object.get("z")),
        PointAdapter.fromJson(object.get("y")));
  }
}
