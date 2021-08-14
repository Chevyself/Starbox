package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.attribute.Attribute;

/** Class to wrap {@link Attribute} to not crash when older versions cannot use it. */
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

  /**
   * Wrap an attribute by finding it by its name.
   *
   * @param name the name of the attribute
   * @return the wrapped attribute
   */
  @NonNull
  public static WrappedAttribute valueOf(@NonNull String name) {
    return new WrappedAttribute(Attribute.valueOf(name));
  }

  /**
   * Get the actual attribute checking that is not null.
   *
   * @return the attribute
   * @throws NullPointerException if there's no wrapped attribute
   */
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
