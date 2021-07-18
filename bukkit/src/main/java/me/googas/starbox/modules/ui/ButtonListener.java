package me.googas.starbox.modules.ui;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This makes the button work and is called by {@link
 * UIModule#onInventoryClick(InventoryClickEvent)} when it is found inside a {@link UI}
 */
public interface ButtonListener {

  /**
   * This method should be implemented to make the button make an action. Cancel the event in order
   * to players not take the item
   *
   * @param event the event of a player clicking inside an inventory
   */
  void onClick(@NonNull InventoryClickEvent event);
}
