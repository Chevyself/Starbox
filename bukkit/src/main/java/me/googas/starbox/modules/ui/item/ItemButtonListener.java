package me.googas.starbox.modules.ui.item;

import lombok.NonNull;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ItemButtonListener {

  /**
   * This method should be implemented to make the button make an action
   *
   * @param event the event of a player interacting with something
   */
  void onClick(@NonNull PlayerInteractEvent event);
}
