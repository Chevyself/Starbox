package me.googas.reflect.wrappers.attributes;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.utility.Versions;

@APIVersion(9)
public class WrappedAttributeModifier {

  @NonNull
  private static final WrappedClass ATTRIBUTE_MODIFIER_CLASS =
      WrappedClass.forName("org.bukkit.attribute.AttributeModifier");

  @NonNull
  private static final WrappedClass OPERATION_CLASS =
      WrappedClass.forName("org.bukkit.attribute.AttributeModifier.Operation");

  @NonNull
  private static final WrappedConstructor<?> ATTRIBUTE_MODIFIER_CONSTRUCTOR =
      WrappedAttributeModifier.ATTRIBUTE_MODIFIER_CLASS.getConstructor(
          UUID.class,
          String.class,
          double.class,
          WrappedAttributeModifier.OPERATION_CLASS.getClazz());

  @NonNull
  private static final WrappedMethod<?> OPERATION_VALUE_OF =
      WrappedAttributeModifier.OPERATION_CLASS.getMethod("valueOf", String.class);

  @NonNull @Getter private final UUID uuid;
  @NonNull @Getter private final String name;
  @Getter private final double amount;
  @NonNull @Getter private final WrappedOperation operation;

  public WrappedAttributeModifier(
      @NonNull UUID uuid,
      @NonNull String name,
      double amount,
      @NonNull WrappedOperation operation) {
    if (name.isEmpty()) throw new IllegalArgumentException("`name` must not be empty");
    this.uuid = uuid;
    this.name = name;
    this.amount = amount;
    this.operation = operation;
  }

  public WrappedAttributeModifier(
      @NonNull String name, double amount, @NonNull WrappedOperation operation) {
    this(UUID.randomUUID(), name, amount, operation);
  }

  public Object toAttributeModifier() {
    return WrappedAttributeModifier.ATTRIBUTE_MODIFIER_CONSTRUCTOR
        .invoke(this.uuid, this.name, this.amount, this.operation.toOperation())
        .provide()
        .orElse(null);
  }

  public enum WrappedOperation {
    ADD_NUMBER,
    ADD_SCALAR,
    MULTIPLY_SCALAR_1;

    /**
     * Get as the actual operation
     *
     * @return the actual operation
     */
    @NonNull
    public Object toOperation() {
      if (Versions.BUKKIT > 9) {
        Object invoke =
            WrappedAttributeModifier.OPERATION_VALUE_OF
                .prepare(null, this.name())
                .provide()
                .orElse(null);
        if (invoke != null) return invoke;
      }
      throw new IllegalStateException(
          "Attempted to get operation in an illegal version of Minecraft");
    }
  }
}
