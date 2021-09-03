package me.googas.starbox.modules.placeholders;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;

/**
 * Must be implemented by placeholders. A placeholder in a raw string is shown inside percentages:
 * %name%, this replaces that with a message
 */
public interface Placeholder {
  /**
   * Get the name of the placeholder. The name that is inside the '%'
   *
   * @return the name
   */
  @NonNull
  String getName();

  /**
   * Build the placeholder.
   *
   * @param player the player that is requesting the placeholder
   * @return the built placeholder
   */
  @NonNull
  String build(OfflinePlayer player);

  /**
   * Placeholder for the player's name.
   */
  class Name implements Placeholder {

    @Override
    public @NonNull String getName() {
      return "name";
    }

    @Override
    public @NonNull String build(OfflinePlayer player) {
      String name = player.getName();
      return name == null ? player.getUniqueId().toString() : name;
    }
  }
}
