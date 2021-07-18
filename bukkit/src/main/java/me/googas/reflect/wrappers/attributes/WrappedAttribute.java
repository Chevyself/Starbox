package me.googas.reflect.wrappers.attributes;

import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.WrappedClass;
import me.googas.reflect.WrappedMethod;
import me.googas.starbox.utility.Versions;

@APIVersion(9)
public enum WrappedAttribute {
  GENERIC_MAX_HEALTH,
  GENERIC_FOLLOW_RANGE,
  GENERIC_KNOCKBACK_RESISTANCE,
  GENERIC_MOVEMENT_SPEED,
  GENERIC_ATTACK_DAMAGE,
  GENERIC_ATTACK_SPEED,
  GENERIC_ARMOR,
  GENERIC_LUCK,
  HORSE_JUMP_STRENGTH,
  ZOMBIE_SPAWN_REINFORCEMENTS;

  @NonNull
  public static final WrappedClass ATTRIBUTE = WrappedClass.parse("org.bukkit.attribute.Attribute");

  @NonNull
  private static final WrappedMethod VALUE_OF =
      WrappedAttribute.ATTRIBUTE.getMethod("valueOf", String.class);

  /**
   * Get as the actual attribute
   *
   * @return the actual attribute
   */
  @NonNull
  public Object toAttribute() {
    if (Versions.BUKKIT >= 9) {
      Object invoke = WrappedAttribute.VALUE_OF.invoke(null, this.name());
      if (invoke != null) return invoke;
    }
    throw new IllegalStateException(
        "Attempted to get attribute in an illegal version of Minecraft");
  }
}
