package me.googas.starbox.modules.ui;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.modules.Module;
import me.googas.starbox.modules.ui.item.ItemButton;
import me.googas.starbox.utility.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * This module makes {@link UI} work. This module is required when a plugin attempts to loadJson
 * custom inventories, else, the events to call the buttons will not work
 */
public class UIModule implements Module {

  @NonNull @Getter private final List<ItemButton> items = new ArrayList<>();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(InventoryClickEvent event) {
    final InventoryHolder holder = event.getInventory().getHolder();
    if (!(holder instanceof UI) || event.getRawSlot() > 53) return;
    ((UI) holder).getButton(event.getRawSlot()).getListener().onClick(event);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    ItemStack hand = Players.getItemInMainHand(event.getPlayer());
    if (hand == null) return;
    ItemButton button = this.getButton(hand);
    if (button != null) button.getListener().onClick(event);
  }

  public ItemButton getButton(@NonNull ItemStack stack) {
    for (ItemButton button : this.items) {
      if (button.getItem().isSimilar(stack)) return button;
    }
    return null;
  }

  @Override
  public @NonNull String getName() {
    return "UI";
  }
}
