package me.googas.starbox.commands.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Provides {@link net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention} to the {@link
 * me.googas.commands.bukkit.CommandManager}.
 */
public class FormatRetentionProvider
    implements BukkitArgumentProvider<ComponentBuilder.FormatRetention> {

  @NonNull private static final List<String> suggestions = new ArrayList<>();

  static {
    for (ComponentBuilder.FormatRetention value : ComponentBuilder.FormatRetention.values()) {
      FormatRetentionProvider.suggestions.add(value.name().toLowerCase());
    }
  }

  @Override
  public @NonNull Class<ComponentBuilder.FormatRetention> getClazz() {
    return ComponentBuilder.FormatRetention.class;
  }

  @Override
  public @NonNull ComponentBuilder.FormatRetention fromString(
      @NonNull String string, @NonNull CommandContext context) throws ArgumentProviderException {
    try {
      return ComponentBuilder.FormatRetention.valueOf(string.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("&c" + string + " did not match any format");
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String s, CommandContext commandContext) {
    return FormatRetentionProvider.suggestions;
  }
}
