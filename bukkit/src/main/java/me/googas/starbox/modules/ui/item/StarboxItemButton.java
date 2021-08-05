package me.googas.starbox.modules.ui.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/** Simple implementation for {@link ItemButton}. */
public class StarboxItemButton implements ItemButton {

  @NonNull @Getter private final ItemButtonListener listener;
  @NonNull @Getter private final ItemStack item;

  /**
   * Create the item button.
   *
   * @param listener the listener to handle actions of the button
   * @param item the item that represents the button
   */
  public StarboxItemButton(@NonNull ItemButtonListener listener, @NonNull ItemStack item) {
    this.listener = listener;
    this.item = item;
  }
}
