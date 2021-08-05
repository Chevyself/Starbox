package me.googas.starbox.addons;

import lombok.NonNull;

/** This object represents an addon created to expand the Starfish bot. */
public interface Addon {

  /**
   * Called when the addon is enabled.
   *
   * @throws Throwable when disabling an addon it may cause an exception that is why * it is thrown
   *     in this method and may be handled by the manager
   */
  default void onEnable() throws Throwable {}

  /**
   * Called when the addon is disabled.
   *
   * @throws Throwable when disabling an addon it may cause an exception that is why it is thrown in
   *     this method and may be handled by the manager
   */
  default void onDisable() throws Throwable {}

  /**
   * Get the addon information.
   *
   * @return the addon information
   */
  @NonNull
  AddonInformation getInformation();
}
