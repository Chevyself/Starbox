package me.googas.starbox.commands.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.utility.Versions;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

/** Provides {@link Enchantment} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class EnchantmentProvider implements BukkitArgumentProvider<Enchantment> {

  @NonNull
  private static final WrappedClass<Enchantment> ENCHANTMENT = WrappedClass.of(Enchantment.class);

  @NonNull
  @APIVersion(since = 8, max = 11)
  private static final WrappedMethod<String> GET_NAME =
      EnchantmentProvider.ENCHANTMENT.getMethod(String.class, "getName");

  @NonNull
  @APIVersion(since = 8, max = 11)
  private static final WrappedMethod<Enchantment> GET_BY_NAME =
      EnchantmentProvider.ENCHANTMENT.getMethod(Enchantment.class, "getByName");

  @NonNull private static final List<String> suggestions = new ArrayList<>();

  static {
    for (Enchantment enchantment : Enchantment.values()) {
      if (Versions.BUKKIT > 11) {
        EnchantmentProvider.suggestions.add(enchantment.getKey().getKey());
      } else {
        EnchantmentProvider.GET_NAME
            .prepare(enchantment)
            .provide()
            .ifPresent(EnchantmentProvider.suggestions::add);
      }
    }
  }

  @Override
  public @NonNull Class<Enchantment> getClazz() {
    return Enchantment.class;
  }

  @Override
  public @NonNull Enchantment fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    Enchantment enchantment;
    if (Versions.BUKKIT > 11) {
      enchantment = Enchantment.getByKey(NamespacedKey.minecraft(string));
    } else {
      enchantment = EnchantmentProvider.GET_BY_NAME.prepare(null, string).provide().orElse(null);
    }
    if (enchantment != null) return enchantment;
    throw new ArgumentProviderException("&cCould not match enchantment with " + string);
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String s, CommandContext commandContext) {
    return EnchantmentProvider.suggestions;
  }
}
