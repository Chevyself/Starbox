package me.googas.reflect.wrappers.properties;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;

/** Wrapper for the inaccessible class 'com.mojang.authlib.properties.PropertyMap'. */
public class WrappedPropertyMap extends StarboxWrapper<Object> {

  @NonNull
  public static final WrappedClass PROPERTY_MAP =
      WrappedClass.forName("com.mojang.authlib.properties.PropertyMap");

  @NonNull
  private static final WrappedConstructor<?> CONSTRUCTOR =
      WrappedPropertyMap.PROPERTY_MAP.getConstructor();

  @NonNull
  private static final WrappedMethod<Boolean> PUT =
      WrappedPropertyMap.PROPERTY_MAP.getMethod(boolean.class, "put", Object.class, Object.class);

  /**
   * Start the wrapper.
   *
   * @param object the object that is supposed to be a 'com.mojang.authlib.properties.PropertyMap'
   */
  public WrappedPropertyMap(@NonNull Object object) {
    super(object);
    if (!object.getClass().equals(WrappedPropertyMap.PROPERTY_MAP.getClazz())) {
      throw new IllegalArgumentException("Expected a PropertyMap received " + object);
    }
  }

  /**
   * Put a property in the map.
   *
   * @param key the key of the property
   * @param value the property
   * @return whether the property has been put
   */
  public boolean put(@NonNull String key, @NonNull WrappedProperty value) {
    return WrappedPropertyMap.PUT
        .prepare(
            this.get().orElseThrow(NullPointerException::new),
            key,
            value.get().orElseThrow(IllegalArgumentException::new))
        .handle(Throwable::printStackTrace)
        .provide()
        .orElse(false);
  }
}
