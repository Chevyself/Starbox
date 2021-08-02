package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;

@APIVersion(9)
public class WrappedAttributeInstance extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass ATTRIBUTE_INSTANCE =
      WrappedClass.forName("org.bukkit.attribute.AttributeInstance");

  @NonNull
  private static final WrappedMethod<?> GET_ATTRIBUTE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("getAttribute");

  @NonNull
  private static final WrappedMethod<Double> GET_BASE_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod(Double.class, "getBaseValue");

  @NonNull
  private static final WrappedMethod<?> SET_BASE_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod("setBaseValue", double.class);

  @NonNull
  private static final WrappedMethod<Double> GET_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod(Double.class, "getValue");

  @NonNull
  private static final WrappedMethod<Double> GET_DEFAULT_VALUE =
      WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getMethod(Double.class, "getDefaultValue");

  public WrappedAttributeInstance(Object reference) {
    super(reference);
    if (reference != null
        && !reference.getClass().equals(WrappedAttributeInstance.ATTRIBUTE_INSTANCE.getClazz())) {
      throw new IllegalArgumentException("Expected a GameProfile received a " + reference);
    }
  }

  public void setBaseValue(double value) {
    WrappedAttributeInstance.SET_BASE_VALUE.prepare(this.get(), value).run();
  }

  @NonNull
  public WrappedAttribute getAttribute() {
    Object invoke =
        WrappedAttributeInstance.GET_ATTRIBUTE.prepare(this.get()).provide().orElse(null);
    if (invoke != null) return WrappedAttribute.valueOf(invoke.toString());
    throw new IllegalStateException(this + " does not have a legal WrappedAttribute");
  }

  public double getBaseValue() {
    return WrappedAttributeInstance.GET_BASE_VALUE.prepare(this.get()).provide().orElse(0.0);
  }

  public double getValue() {
    return WrappedAttributeInstance.GET_VALUE.prepare(this.get()).provide().orElse(0.0);
  }

  public double getDefaultValue() {
    return WrappedAttributeInstance.GET_DEFAULT_VALUE.prepare(this.get()).provide().orElse(0.0);
  }
}
