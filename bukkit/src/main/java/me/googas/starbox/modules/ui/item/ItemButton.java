package me.googas.starbox.modules.ui.item;

import lombok.NonNull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Interface to create in-game buttons. Item buttons are items that when on {@link
 * org.bukkit.event.player.PlayerInteractEvent} will run {@link
 * ItemButtonListener#onClick(PlayerInteractEvent)}
 */
public interface ItemButton {

  /**
   * Get the listener of the button.
   *
   * @return the listener of the button
   */
  @NonNull
  ItemButtonListener getListener();

  /**
   * Get the item of the button.
   *
   * @return the item that represents this button.
   */
  @NonNull
  ItemStack getItem();
}
