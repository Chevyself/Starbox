package me.googas.starbox.reflect.wrappers.properties;

import lombok.NonNull;
import me.googas.starbox.reflect.WrappedClass;
import me.googas.starbox.reflect.WrappedConstructor;
import me.googas.starbox.reflect.wrappers.SimpleWrapper;

public class WrappedProperty extends SimpleWrapper {

  @NonNull
  public static final WrappedClass PROPERTY =
      WrappedClass.parse("com.mojang.authlib.properties.Property");

  @NonNull
  private static final WrappedConstructor PROPERTY_CONSTRUCTOR =
      WrappedProperty.PROPERTY.getConstructor(String.class, String.class, String.class);

  @NonNull
  private static final WrappedConstructor PROPERTY_KEY_VAL_CONSTRUCTOR =
      WrappedProperty.PROPERTY.getConstructor(String.class, String.class);

  public WrappedProperty(@NonNull Object reference) {
    super(reference);
    if (!reference.getClass().equals(WrappedProperty.PROPERTY.getClazz())) {
      throw new IllegalArgumentException("Expected a Property received a " + reference);
    }
  }

  @NonNull
  public static WrappedProperty construct(@NonNull String key, @NonNull String value) {
    Object invoke = WrappedProperty.PROPERTY_KEY_VAL_CONSTRUCTOR.invoke(key, value);
    if (invoke != null) {
      return new WrappedProperty(invoke);
    } else {
      throw new IllegalArgumentException("Property could not be created");
    }
  }

  public static WrappedProperty construct(
      @NonNull String key, @NonNull String value, String signature) {
    Object invoke = WrappedProperty.PROPERTY_CONSTRUCTOR.invoke(key, value, signature);
    if (invoke != null) {
      return new WrappedProperty(invoke);
    } else {
      throw new IllegalArgumentException("Property could not be created");
    }
  }
}
