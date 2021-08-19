package me.googas.starbox.commands.providers;

import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.ChatColor;

/** Provides {@link ChatColor} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class BungeeChatColorProvider implements BukkitArgumentProvider<ChatColor> {

  @NonNull
  private static final List<String> suggestions =
      Arrays.asList(
          "dark_blue",
          "dark_green",
          "dark_aqua",
          "dark_red",
          "dark_purple",
          "gold",
          "gray",
          "dark_gray",
          "blue",
          "green",
          "aqua",
          "red",
          "light_purple",
          "yellow",
          "white",
          "obfuscated",
          "bold",
          "strikethrough",
          "underline",
          "italic",
          "reset");

  @Override
  public @NonNull Class<ChatColor> getClazz() {
    return ChatColor.class;
  }

  @Override
  public @NonNull ChatColor fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    try {
      return ChatColor.of(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException("&c" + string + " did not match any color");
    }
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return BungeeChatColorProvider.suggestions;
  }
}
