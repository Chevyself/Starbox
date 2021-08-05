package me.googas.starbox.modules.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;
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

  @NonNull @Getter @Delegate private final List<ItemButton> items = new ArrayList<>();

  /**
   * Check if inventory click is in a {@link UI}. if the inventory holder is an {@link UI} it will
   * get its button and run
   *
   * @param event the event of an inventory being clicked
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(InventoryClickEvent event) {
    final InventoryHolder holder = event.getInventory().getHolder();
    if (!(holder instanceof UI) || event.getRawSlot() > 53) return;
    ((UI) holder).getButton(event.getRawSlot()).getListener().onClick(event);
  }

  /**
   * Check if an interaction is done with a button item. If the item happens to be a button the
   * event will fire its listener
   *
   * @param event the event of a player interacting
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    Players.getItemInMainHand(event.getPlayer())
        .flatMap(this::getButton)
        .ifPresent(button -> button.getListener().onClick(event));
  }

  /**
   * Get a button from its ItemStack.
   *
   * @param stack the stack to match the button
   * @return a {@link Optional} instance holding the matched button
   */
  @NonNull
  public Optional<ItemButton> getButton(@NonNull ItemStack stack) {
    return this.items.stream().filter(button -> button.getItem().isSimilar(stack)).findFirst();
  }

  @Override
  public @NonNull String getName() {
    return "UI";
  }
}
