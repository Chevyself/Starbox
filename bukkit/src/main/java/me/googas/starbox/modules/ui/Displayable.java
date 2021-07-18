package me.googas.starbox.modules.ui;

import lombok.NonNull;
import me.googas.starbox.modules.ui.buttons.StarboxButton;
import me.googas.starbox.modules.ui.item.ItemButton;
import me.googas.starbox.modules.ui.item.ItemButtonListener;
import me.googas.starbox.modules.ui.item.StarboxItemButton;
import org.bukkit.inventory.ItemStack;

/** A displayable is an object which can be represented as a button */
public interface Displayable {

  /**
   * Create the item stack which represents the object
   *
   * @return the item
   */
  @NonNull
  ItemStack toItem();

  /**
   * Create the button which represents the object
   *
   * @return the button
   */
  @NonNull
  default Button toButton(@NonNull ButtonListener listener) {
    return new StarboxButton(listener, this.toItem());
  }

  /**
   * Create the item button which represents the object
   *
   * @return the button
   */
  @NonNull
  default ItemButton toItemButton(@NonNull ItemButtonListener listener) {
    return new StarboxItemButton(listener, this.toItem());
  }
}
