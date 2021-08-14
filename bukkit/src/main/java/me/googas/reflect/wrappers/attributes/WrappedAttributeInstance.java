package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.attribute.AttributeInstance;

/** Class to wrap {@link AttributeInstance} to not crash when older versions cannot use it. */
@APIVersion(since = 9)
public class WrappedAttributeInstance extends StarboxWrapper<AttributeInstance> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedAttributeInstance(AttributeInstance reference) {
    super(reference);
  }

  /**
   * Get the actual attribute instance checking that is not null.
   *
   * @return the attribute instance
   * @throws NullPointerException if there's no wrapped attribute instance
   */
  @NonNull
  @Delegate
  public AttributeInstance getAttributeInstance() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedAttributeInstance set(AttributeInstance object) {
    return (WrappedAttributeInstance) super.set(object);
  }
}
