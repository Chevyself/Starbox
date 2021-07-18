package me.googas.starbox.addons;

import lombok.NonNull;

/** This object represents an addon created to expand the Starfish bot */
public interface Addon {

  /** Called when the addon is enabled */
  default void onEnable() throws Throwable {}

  /** Called when the addon is disabled */
  default void onDisable() throws Throwable {}

  /**
   * Get the addon information
   *
   * @return the addon information
   */
  @NonNull
  AddonInformation getInformation();
}
