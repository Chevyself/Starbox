package me.googas.reflect.wrappers.attributes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.starbox.builders.Builder;
import me.googas.starbox.utility.Versions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * Class to wrap {@link Multimap} having {@link Attribute} and {@link AttributeModifier} to not
 * crash when older versions cannot use it.
 */
@APIVersion(since = 9)
public class WrappedAttributes implements Builder<Multimap<Attribute, AttributeModifier>> {

  @NonNull @Getter @Delegate private final Map<WrappedAttribute, WrappedAttributeModifier> map;

  /**
   * Create the attributes.
   *
   * @param map the map of the attributes
   */
  public WrappedAttributes(@NonNull Map<WrappedAttribute, WrappedAttributeModifier> map) {
    this.map = map;
  }

  /**
   * Create the attributes.
   */
  public WrappedAttributes() {
    this(new HashMap<>());
  }

  @Override
  public @NonNull Multimap<Attribute, AttributeModifier> build() {
    Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
    this.map.forEach(
        (key, value) -> multimap.put(key.getAttribute(), value.getAttributeModifier()));
    return multimap;
  }

  /**
   * Json adapter for {@link WrappedAttribute} to register in {@link com.google.gson.Gson}.
   */
  public static class Adapter
      implements JsonDeserializer<WrappedAttributes>, JsonSerializer<WrappedAttributes> {

    @Override
    public JsonElement serialize(
        WrappedAttributes src, Type typeOfSrc, JsonSerializationContext context) {
      if (Versions.BUKKIT > 8) {
        context.serialize(src.getMap());
      }
      return null;
    }

    @Override
    public WrappedAttributes deserialize(
        JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      if (Versions.BUKKIT > 8) {
        return new WrappedAttributes(
            context.deserialize(
                json,
                WrappedClass.of(WrappedAttributes.class)
                    .getDeclaredField("map")
                    .getField()
                    .getGenericType()));
      }
      return null;
    }
  }
}
