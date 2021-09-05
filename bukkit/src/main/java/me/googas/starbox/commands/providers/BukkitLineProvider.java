package me.googas.starbox.commands.providers;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitMultiArgumentProvider;
import me.googas.starbox.BukkitLine;
import me.googas.starbox.Strings;

/** Provides {@link BukkitLine} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class BukkitLineProvider implements BukkitMultiArgumentProvider<BukkitLine> {
  @Override
  public @NonNull List<String> getSuggestions(@NonNull CommandContext commandContext) {
    return Collections.singletonList("$");
  }

  @Override
  public @NonNull BukkitLine fromStrings(
      @NonNull String[] strings, @NonNull CommandContext context) {
    String string = Strings.fromArray(strings);
    return BukkitLine.parse(string.trim());
  }

  @Override
  public @NonNull Class<BukkitLine> getClazz() {
    return BukkitLine.class;
  }
}
