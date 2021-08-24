package me.googas.starbox.commands.providers;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.reflect.wrappers.inventory.WrappedEnchantment;
import org.bukkit.enchantments.Enchantment;

/** Provides {@link Enchantment} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class EnchantmentProvider implements BukkitArgumentProvider<Enchantment> {

  @NonNull
  private static final List<String> suggestions =
      WrappedEnchantment.values().stream()
          .map(wrap -> wrap.getName().toLowerCase())
          .collect(Collectors.toList());

  @Override
  public @NonNull Class<Enchantment> getClazz() {
    return Enchantment.class;
  }

  @Override
  public @NonNull Enchantment fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    try {
      return WrappedEnchantment.valueOf(string).getEnchantment();
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("&cCould not match enchantment with " + string);
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String s, CommandContext commandContext) {
    return EnchantmentProvider.suggestions;
  }
}
