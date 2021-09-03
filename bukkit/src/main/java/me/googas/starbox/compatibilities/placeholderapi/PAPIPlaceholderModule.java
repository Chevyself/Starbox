package me.googas.starbox.compatibilities.placeholderapi;

import java.util.Optional;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.googas.starbox.Starbox;
import me.googas.starbox.modules.Module;
import me.googas.starbox.modules.placeholders.Placeholder;
import me.googas.starbox.modules.placeholders.PlaceholderModule;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/** Allows building messages with placeholders using PAPI. */
public class PAPIPlaceholderModule implements Module {

  /**
   * Build an {@link String}.
   *
   * @param player the player to build the placeholders to
   * @param raw the raw string
   * @return the built string
   */
  @NonNull
  public String build(@NonNull OfflinePlayer player, @NonNull String raw) {
    return PlaceholderAPI.setPlaceholders(player, raw);
  }

  @Override
  public @NonNull String getName() {
    return "papi-placeholders";
  }

  /** Represents a {@link PlaceholderExpansion} created by 'Starbox'. */
  public static class StarboxPlaceholderExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
      return "starbox";
    }

    @Override
    public @NotNull String getAuthor() {
      return "Starbox";
    }

    @Override
    public @NotNull String getVersion() {
      return Starbox.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
      return true;
    }

    @Override
    public boolean canRegister() {
      return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NonNull String name) {
      return this.getPlaceholder(name.startsWith("starbox.") ? name.substring(8) : name)
          .map(placeholder -> placeholder.build(player))
          .orElse(null);
    }

    @NonNull
    private Optional<Placeholder> getPlaceholder(@NonNull String name) {
      return Starbox.getModules()
          .get(PlaceholderModule.class)
          .map(module -> module.getPlaceholder(name));
    }
  }
}
