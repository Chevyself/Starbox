package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.attribute.Attribute;

@APIVersion(since = 9)
public class WrappedAttribute extends StarboxWrapper<Attribute> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedAttribute(@NonNull Attribute reference) {
    super(reference);
  }

  @NonNull
  public static WrappedAttribute valueOf(@NonNull String name) {
    return new WrappedAttribute(Attribute.valueOf(name));
  }

  @NonNull
  @Delegate
  public Attribute getAttribute() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedAttribute set(@NonNull Attribute object) {
    return (WrappedAttribute) super.set(object);
  }
}
