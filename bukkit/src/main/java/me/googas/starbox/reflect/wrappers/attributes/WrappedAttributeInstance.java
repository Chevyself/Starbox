package me.googas.starbox.reflect.wrappers.attributes;

import lombok.NonNull;
import me.googas.starbox.reflect.APIVersion;
import me.googas.starbox.reflect.WrappedClass;
import me.googas.starbox.reflect.WrappedMethod;
import me.googas.starbox.reflect.wrappers.SimpleWrapper;

@APIVersion(9)
public class WrappedAttributeInstance extends SimpleWrapper {

  @NonNull
  private static final WrappedClass ATTRIBUTE_INSTANCE =
      WrappedClass.parse("org.bukkit.attribute.AttributeInstance");

  @NonNull
  private static final WrappedMethod GET_ATTRIBUTE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("getAttribute");

  @NonNull
  private static final WrappedMethod GET_BASE_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("getBaseValue");

  @NonNull
  private static final WrappedMethod SET_BASE_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("setBaseValue", double.class);

  @NonNull
  private static final WrappedMethod GET_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("getValue");

  @NonNull
  private static final WrappedMethod GET_DEFAULT_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("getDefaultValue");

  public WrappedAttributeInstance(Object reference) {
    super(reference);
    if (reference != null
        && !reference.getClass().equals(WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getClazz())) {
      throw new IllegalArgumentException("Expected a GameProfile received a " + reference);
    }
  }

  public void setBaseValue(double value) {
    WrappedAttributeInstance.SET_BASE_VALUE.invoke(this.get(), value);
  }

  @NonNull
  public WrappedAttribute getAttribute() {
    Object invoke = WrappedAttributeInstance.GET_ATTRIBUTE.invoke(this.get());
    if (invoke != null) return WrappedAttribute.valueOf(invoke.toString());
    throw new IllegalStateException(this + " does not have a legal WrappedAttribute");
  }

  public double getBaseValue() {
    Object invoke = WrappedAttributeInstance.GET_BASE_VALUE.invoke(this.get());
    if (invoke instanceof Double) {
      return (double) invoke;
    }
    return 0;
  }

  public double getValue() {
    Object invoke = WrappedAttributeInstance.GET_VALUE.invoke(this.get());
    if (invoke instanceof Double) {
      return (double) invoke;
    }
    return 0;
  }

  public double getDefaultValue() {
    Object invoke = WrappedAttributeInstance.GET_DEFAULT_VALUE.invoke(this.get());
    if (invoke instanceof Double) {
      return (double) invoke;
    }
    return 0;
  }
}
