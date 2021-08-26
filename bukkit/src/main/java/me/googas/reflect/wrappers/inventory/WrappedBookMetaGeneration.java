package me.googas.reflect.wrappers.inventory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import me.googas.starbox.utility.Versions;
import org.bukkit.inventory.meta.BookMeta;

/** Class to wrap {@link BookMeta.Generation} to not crash when older versions cannot use it. */
@APIVersion(since = 9)
public class WrappedBookMetaGeneration extends StarboxWrapper<BookMeta.Generation> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedBookMetaGeneration(@NonNull BookMeta.Generation reference) {
    super(reference);
  }

  /**
   * Get the actual book generation checking that is not null.
   *
   * @return the generation
   * @throws NullPointerException if there's no generation
   */
  @NonNull
  @Delegate
  public BookMeta.Generation getGeneration() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedBookMetaGeneration set(@NonNull BookMeta.Generation object) {
    return (WrappedBookMetaGeneration) super.set(object);
  }

  /** Represents the adapter for {@link com.google.gson.Gson}. */
  public static class Adapter
      implements JsonDeserializer<WrappedBookMetaGeneration>,
          JsonSerializer<WrappedBookMetaGeneration> {

    @Override
    public JsonElement serialize(
        WrappedBookMetaGeneration src, Type type, JsonSerializationContext context) {
      if (Versions.BUKKIT > 8) {
        return context.serialize(src.getGeneration());
      }
      return null;
    }

    @Override
    public WrappedBookMetaGeneration deserialize(
        JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
      if (Versions.BUKKIT > 8) {
        return new WrappedBookMetaGeneration(context.deserialize(json, BookMeta.Generation.class));
      }
      return null;
    }
  }
}
