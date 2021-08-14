package me.googas.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Adapts {@link OfflinePlayer} to Json. This makes that {@link OfflinePlayer} is adapted using its
 * {@link UUID}
 */
public class OfflinePlayerAdapter
    implements JsonSerializer<OfflinePlayer>, JsonDeserializer<OfflinePlayer> {
  @Override
  public JsonElement serialize(
      OfflinePlayer src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getUniqueId().toString());
  }

  @Override
  public OfflinePlayer deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return Bukkit.getOfflinePlayer(UUID.fromString(json.getAsJsonPrimitive().getAsString()));
  }
}
