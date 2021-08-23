package me.googas.reflect.wrappers.inventory;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

/** Wraps {@link Enchantment} so methods can be unified between versions. */
public class WrappedEnchantment extends StarboxWrapper<Enchantment> {

  @NonNull
  private static final WrappedClass<Enchantment> ENCHANTMENT = WrappedClass.of(Enchantment.class);

  @NonNull
  @APIVersion(since = 8, max = 11)
  private static final WrappedMethod<String> GET_NAME =
      WrappedEnchantment.ENCHANTMENT.getMethod(String.class, "getName");

  @NonNull
  @APIVersion(since = 8, max = 11)
  private static final WrappedMethod<Enchantment> GET_BY_NAME =
      WrappedEnchantment.ENCHANTMENT.getMethod(Enchantment.class, "getByName", String.class);

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  private WrappedEnchantment(@NonNull Enchantment reference) {
    super(reference);
  }

  /**
   * Wrap an enchantment by finding it by its name. This will get the enchantment by its name prior
   * 1.12 and by its key in newer versions
   *
   * @param string the name to match the enchantment
   * @return the wrapped enchantment
   * @throws IllegalArgumentException if the enchantment could not be matched
   */
  @NonNull
  public static WrappedEnchantment valueOf(@NonNull String string) {
    Enchantment enchantment;
    if (Versions.BUKKIT > 11) {
      enchantment = Enchantment.getByKey(NamespacedKey.minecraft(string));
    } else {
      enchantment =
          WrappedEnchantment.GET_BY_NAME
              .prepare(null, string.toUpperCase())
              .handle(Starbox::severe)
              .provide()
              .orElse(null);
    }
    if (enchantment != null) return new WrappedEnchantment(enchantment);
    throw new IllegalArgumentException("Could not match enchantment by the name/key " + string);
  }

  /**
   * Wrap an enchantment.
   *
   * @param enchantment the enchantment to be wrapped
   * @return the wrapper
   */
  @NonNull
  public static WrappedEnchantment of(@NonNull Enchantment enchantment) {
    return new WrappedEnchantment(enchantment);
  }

  /**
   * Get all the enchantments wrapped and in a list.
   *
   * @return the values
   */
  @NonNull
  public static List<WrappedEnchantment> values() {
    List<WrappedEnchantment> wrappers = new ArrayList<>(Enchantment.values().length);
    for (Enchantment enchantment : Enchantment.values()) {
      wrappers.add(new WrappedEnchantment(enchantment));
    }
    return wrappers;
  }

  /**
   * Get the name of the enchantment. This is the key for newer versions and the name prior 1.12
   *
   * @return the name of the enchantment
   */
  @NonNull
  public String getName() {
    if (Versions.BUKKIT > 11) {
      return this.getEnchantment().getKey().getKey();
    } else {
      return WrappedEnchantment.GET_NAME
          .prepare(this.getEnchantment())
          .handle(Starbox::severe)
          .provide()
          .orElseThrow(NullPointerException::new);
    }
  }

  /**
   * Get the actual enchantment.
   *
   * @return the enchantment
   */
  @NonNull
  public Enchantment getEnchantment() {
    return this.get().orElseThrow(NullPointerException::new);
  }
}
