package me.googas.starbox.modules.ui;

import lombok.NonNull;
import org.bukkit.inventory.InventoryHolder;

/**
 * UI stands for User Interface which in Minecraft is created as an Inventory.
 *
 * <p>The slots of the UI start from 0 in the top left to 53 in the bottom right
 */
public interface UI extends InventoryHolder {

  /**
   * Get the button of the given position
   *
   * @param position the position to get the button on
   * @return the button if there's one in the position else null
   */
  @NonNull
  Button getButton(int position);

  /**
   * Set the button in the given position
   *
   * @param position the position to set the button on
   * @param button the button to be set in the position
   * @return this same instance
   */
  @NonNull
  UI set(int position, @NonNull Button button);

  /**
   * Removes the button in the given position
   *
   * @param position the position to remove the button
   * @return this same instance
   */
  @NonNull
  UI remove(int position);
}
