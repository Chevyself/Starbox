package me.googas.starbox.modules;

import lombok.NonNull;
import me.googas.starbox.Starbox;
import org.bukkit.event.Listener;

/**
 * A module is a Bukkit listener which can be injected at any point in runtime.
 *
 * <p>Modules are provided by Starbox or a compatibility, also can be implemented and added to a
 * {@link ModuleRegistry} to be enabled
 */
public interface Module extends Listener {

  /** Called when the module is engaged in the {@link ModuleRegistry} */
  default void onEnable() {
    Starbox.getPlugin().getLogger().info(this.getName() + " has been engaged");
  }

  /** Called when the module is disengaged in the {@link ModuleRegistry} */
  default void onDisable() {
    Starbox.getPlugin().getLogger().info(this.getName() + " has been disengaged");
  }

  /**
   * Get the name of the module. This is useful to identify modules in the {@link ModuleRegistry}
   *
   * @return the name of the module
   */
  @NonNull
  String getName();
}
