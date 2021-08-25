package me.googas.starbox.compatibilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

  @NonNull @Getter private final List<Compatibility> compatibilities;

  /**
   * Create the compatibility manager.
   *
   * @param compatibilities the compatibilities available to check
   */
  public CompatibilityManager(@NonNull List<Compatibility> compatibilities) {
    this.compatibilities = compatibilities;
  }

  /** Create the compatibility manager. */
  public CompatibilityManager() {
    this(new ArrayList<>());
  }

  /**
   * Add a compatibility to the manager.
   *
   * @param compatibility the compatibility to add
   * @return this same instance
   */
  @NonNull
  public CompatibilityManager add(@NonNull Compatibility compatibility) {
    this.compatibilities.add(compatibility);
    return this;
  }

  /**
   * Add many compatibilities to the manager.
   *
   * @param compatibilities the collection of compatibility to add
   * @return this same instance
   */
  @NonNull
  public CompatibilityManager addAll(@NonNull Collection<? extends Compatibility> compatibilities) {
    this.compatibilities.addAll(compatibilities);
    return this;
  }

  /**
   * Add many compatibilities to the manager.
   *
   * @param compatibilities the array of compatibility to add
   * @return this same instance
   */
  @NonNull
  public CompatibilityManager addAll(@NonNull Compatibility... compatibilities) {
    return this.addAll(Arrays.asList(compatibilities));
  }

  /**
   * This method will check whether the compatibilities are loaded getting the plugin manager. This
   * is where the name of the compatibility is required. If the compatibility is set to enable
   * {@link Compatibility#setEnabled(boolean)} {@link Compatibility#onEnable()} will be executed
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
