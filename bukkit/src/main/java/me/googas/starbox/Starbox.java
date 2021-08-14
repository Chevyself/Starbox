package me.googas.starbox;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;

/** Static access for {@link StarboxPlugin}. */
public class Starbox {

  @Getter private static StarboxPlugin instance;

  public static void handle(@NonNull Exception e) {
    Starbox.getLogger().log(Level.SEVERE, e, () -> "");
  }

  /**
   * Set the instance of the plugin.
   *
   * @param instance the instance of the plugin
   * @throws IllegalStateException if the plugin is already initialized
   */
  public static void setInstance(StarboxPlugin instance) {
    if (Starbox.instance != null && instance != null)
      throw new IllegalStateException("Plugin is already initialized");
    Starbox.instance = instance;
  }

  /**
   * Get the plugin.
   *
   * @return the plugin
   * @throws NullPointerException if the plugin has not been initialized
   */
  @NonNull
  public static StarboxPlugin getPlugin() {
    return Objects.requireNonNull(Starbox.instance, "Starbox has not been initialized");
  }

  /**
   * Get the logger of the plugin.
   *
   * @return the logger
   * @throws NullPointerException if the plugin has not been initialized
   */
  @NonNull
  public static Logger getLogger() {
    return Starbox.getPlugin().getLogger();
  }
}
