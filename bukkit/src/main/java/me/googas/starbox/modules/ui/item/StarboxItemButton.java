package me.googas.starbox.modules.ui.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

public class StarboxItemButton implements ItemButton {

  @NonNull @Getter private final ItemButtonListener listener;
  @NonNull @Getter private final ItemStack item;

  public StarboxItemButton(@NonNull ItemButtonListener listener, @NonNull ItemStack item) {
    this.listener = listener;
    this.item = item;
  }
}
