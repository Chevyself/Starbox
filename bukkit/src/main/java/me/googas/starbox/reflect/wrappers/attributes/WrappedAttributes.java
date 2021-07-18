package me.googas.starbox.reflect.wrappers.attributes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.starbox.builders.Builder;

public class WrappedAttributes implements Builder<Multimap<Object, Object>> {

  @NonNull @Delegate
  private final Map<WrappedAttribute, WrappedAttributeModifier> map = new HashMap<>();

  @Override
  public @NonNull Multimap<Object, Object> build() {
    ArrayListMultimap<Object, Object> multimap = ArrayListMultimap.create();
    this.map.forEach((key, value) -> multimap.put(key.toAttribute(), value.toAttributeModifier()));
    return multimap;
  }
}
