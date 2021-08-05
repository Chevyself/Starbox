package me.googas.reflect.wrappers.properties;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;

/** Wraps the class 'com.mojang.authlib,properties.Property'. */
public class WrappedProperty extends StarboxWrapper<Object> {

  @NonNull
  public static final WrappedClass PROPERTY =
      WrappedClass.forName("com.mojang.authlib.properties.Property");

  @NonNull
  private static final WrappedConstructor<?> PROPERTY_CONSTRUCTOR =
      WrappedProperty.PROPERTY.getConstructor(String.class, String.class, String.class);

  @NonNull
  private static final WrappedConstructor<?> PROPERTY_KEY_VAL_CONSTRUCTOR =
      WrappedProperty.PROPERTY.getConstructor(String.class, String.class);

  /**
   * Wrap a Property.
   *
   * @param reference the object that is the property
   */
  public WrappedProperty(@NonNull Object reference) {
    super(reference);
    if (!reference.getClass().equals(WrappedProperty.PROPERTY.getClazz())) {
      throw new IllegalArgumentException("Expected a Property received a " + reference);
    }
  }

  /**
   * Create a property.
   *
   * @param key the key of the property
   * @param value the value of the property
   * @return the wrapper of the property
   */
  @NonNull
  public static WrappedProperty construct(@NonNull String key, @NonNull String value) {
    Object invoke =
        WrappedProperty.PROPERTY_KEY_VAL_CONSTRUCTOR
            .invoke(key, value)
            .provide()
            .orElseThrow(() -> new IllegalArgumentException("Property could not be created"));
    return new WrappedProperty(invoke);
  }

  /**
   * Create a property.
   *
   * @param key the key of the property
   * @param value the value of the property
   * @param signature the signature of the property
   * @return the wrapper of the property
   */
  @NonNull
  public static WrappedProperty construct(
      @NonNull String key, @NonNull String value, String signature) {
    Object object =
        WrappedProperty.PROPERTY_CONSTRUCTOR
            .invoke(key, value, signature)
            .provide()
            .orElseThrow(() -> new IllegalArgumentException("Property could not be created"));
    return new WrappedProperty(object);
  }
}
