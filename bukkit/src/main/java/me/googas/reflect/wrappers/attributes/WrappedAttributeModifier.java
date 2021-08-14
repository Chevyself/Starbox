package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.attribute.AttributeModifier;

/** Class to wrap {@link AttributeModifier} to not crash when older versions cannot use it. */
@APIVersion(since = 9)
public class WrappedAttributeModifier extends StarboxWrapper<AttributeModifier> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedAttributeModifier(@NonNull AttributeModifier reference) {
    super(reference);
  }

  /**
   * Get the actual attribute modifier checking that is not null.
   *
   * @return the attribute instance
   * @throws NullPointerException if there's no wrapped attribute modifier
   */
  @NonNull
  @Delegate
  public AttributeModifier getAttributeModifier() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedAttributeModifier set(@NonNull AttributeModifier object) {
    return (WrappedAttributeModifier) super.set(object);
  }
}
