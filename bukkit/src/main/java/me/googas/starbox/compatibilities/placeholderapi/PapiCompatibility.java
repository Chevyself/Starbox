package me.googas.starbox.compatibilities.placeholderapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.StarboxContextualProvider;
import me.googas.starbox.compatibilities.Compatibility;
import me.googas.starbox.modules.Module;
import org.bukkit.plugin.Plugin;

/** Compatibility with 'PlaceholderAPI' to add many placeholders. */
public class PapiCompatibility implements Compatibility {

  @Getter @Setter private boolean enabled;

  @Override
  public @NonNull Collection<Module> getModules(@NonNull Plugin plugin) {
    return Collections.singletonList(new PAPIPlaceholderModule());
  }

  @Override
  public Collection<StarboxContextualProvider<?, CommandContext>> getProviders() {
    return new ArrayList<>();
  }

  @Override
  public @NonNull Collection<StarboxBukkitCommand> getCommands() {
    return new ArrayList<>();
  }

  @Override
  public @NonNull String getName() {
    return "PlaceholderAPI";
  }
}
