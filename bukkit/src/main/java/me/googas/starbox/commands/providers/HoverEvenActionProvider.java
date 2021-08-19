package me.googas.starbox.commands.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.chat.HoverEvent;

/** Provides {@link HoverEvent.Action} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class HoverEvenActionProvider implements BukkitArgumentProvider<HoverEvent.Action> {

  @NonNull private static final List<String> suggestions = new ArrayList<>();

  static {
    for (HoverEvent.Action value : HoverEvent.Action.values()) {
      HoverEvenActionProvider.suggestions.add(value.name().toLowerCase());
    }
  }

  @Override
  public @NonNull Class<HoverEvent.Action> getClazz() {
    return HoverEvent.Action.class;
  }

  @Override
  public @NonNull HoverEvent.Action fromString(
      @NonNull String string, @NonNull CommandContext context) throws ArgumentProviderException {
    try {
      return HoverEvent.Action.valueOf(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("&c" + string + " did not match any action");
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return HoverEvenActionProvider.suggestions;
  }
}
