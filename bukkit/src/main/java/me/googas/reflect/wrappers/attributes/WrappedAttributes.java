package me.googas.reflect.wrappers.attributes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.starbox.builders.Builder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

@APIVersion(since = 9)
public class WrappedAttributes implements Builder<Multimap<Attribute, AttributeModifier>> {

  @NonNull @Delegate
  private final Map<WrappedAttribute, WrappedAttributeModifier> map = new HashMap<>();

  @Override
  public @NonNull Multimap<Attribute, AttributeModifier> build() {
    ArrayListMultimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
    this.map.forEach(
        (key, value) -> multimap.put(key.getAttribute(), value.getAttributeModifier()));
    return multimap;
  }
}
