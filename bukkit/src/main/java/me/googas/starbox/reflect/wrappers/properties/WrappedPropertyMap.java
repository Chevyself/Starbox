package me.googas.starbox.reflect.wrappers.properties;

import lombok.NonNull;
import me.googas.starbox.reflect.WrappedClass;
import me.googas.starbox.reflect.WrappedConstructor;
import me.googas.starbox.reflect.WrappedMethod;
import me.googas.starbox.reflect.wrappers.SimpleWrapper;

public class WrappedPropertyMap extends SimpleWrapper {

  @NonNull
  private static final WrappedClass PROPERTY_MAP =
      WrappedClass.parse("com.mojang.authlib.properties.PropertyMap");

  @NonNull
  private static final WrappedConstructor CONSTRUCTOR =
      WrappedPropertyMap.PROPERTY_MAP.getConstructor();

  @NonNull
  private static final WrappedMethod PUT =
      WrappedPropertyMap.PROPERTY_MAP.getMethod("put", Object.class, Object.class);

  public WrappedPropertyMap(@NonNull Object object) {
    super(object);
    if (!object.getClass().equals(WrappedPropertyMap.PROPERTY_MAP.getClazz())) {
      throw new IllegalArgumentException("Expected a PropertyMap received " + object);
    }
  }

  @NonNull
  public static WrappedPropertyMap construct() {
    return new WrappedPropertyMap(WrappedPropertyMap.CONSTRUCTOR.invoke());
  }

  public boolean put(@NonNull String key, @NonNull WrappedProperty value) {
    Object invoke = WrappedPropertyMap.PUT.invoke(this.get(), key, value.get());
    if (invoke instanceof Boolean) {
      return (boolean) invoke;
    } else {
      return false;
    }
  }
}
