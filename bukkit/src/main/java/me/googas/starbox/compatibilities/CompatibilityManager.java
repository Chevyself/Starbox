package me.googas.starbox.compatibilities;


import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

/**
 * This object is required in order to check whether the compatibilities are loaded or can be
 * loaded.
 *
 * @see Compatibility
 */
public class CompatibilityManager {

  @NonNull @Getter private final Set<Compatibility> compatibilities;

  /**
   * Create the compatibility manager.
   *
   * @param compatibilities the compatibilities available to check
   */
  public CompatibilityManager(@NonNull Set<Compatibility> compatibilities) {
    this.compatibilities = compatibilities;
  }

  /**
   * Create the compatibility manager.
   */
  public CompatibilityManager() {
    this(new HashSet<>());
  }

  /**
   * This method will check whether the compatibilities are loaded getting the plugin manager. This
   * is where the name of the compatibility is required. If the compatibility is set to enable {@link Compatibility#setEnabled(boolean)}
   * {@link Compatibility#onEnable()} will be executed
   *
   * @see Compatibility#getName()
   * @return this same instance
   */
  public CompatibilityManager check() {
    for (Compatibility compatibility : this.compatibilities) {
      if (Bukkit.getServer().getPluginManager().getPlugin(compatibility.getName()) != null) {
        compatibility.setEnabled(true);
        compatibility.onEnable();
      }
    }
     return this;
  }

  /**
   * Check whether a compatibility is enabled.
   *
   * @param name the name of the compatibility
   * @return true if the compatibility is loaded
   */
  public boolean isEnabled(@NonNull String name) {
    return this.getCompatibility(name).isEnabled();
  }

  /**
   * Get a compatibility by its name.
   *
   * @param name the name of the compatibility
   * @return the compatibility
   * @throws IllegalArgumentException if the name does not match a compatibility
   */
  @NonNull
  public Compatibility getCompatibility(@NonNull String name) {
    for (Compatibility compatibility : this.compatibilities) {
      if (compatibility.getName().equalsIgnoreCase(name)) {
        return compatibility;
      }
    }
    throw new IllegalArgumentException(name + " is not a dependency");
  }
}