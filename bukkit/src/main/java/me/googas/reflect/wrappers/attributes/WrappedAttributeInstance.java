package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.attribute.AttributeInstance;

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
