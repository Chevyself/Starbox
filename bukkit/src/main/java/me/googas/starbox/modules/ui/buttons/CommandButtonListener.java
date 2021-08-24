package me.googas.starbox.modules.ui.buttons;

import lombok.NonNull;
import me.googas.starbox.modules.ui.ButtonListener;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;

/** This type of listener is used for when a player clicks a button it will run a command. */
public class CommandButtonListener implements ButtonListener {

  @NonNull private final String command;

  /**
   * Create the listener.
   *
   * @param command the command to execute when clicked
   */
  public CommandButtonListener(@NonNull String command) {
    this.command = command;
  }

  @Override
  public void onClick(@NonNull InventoryClickEvent event) {
    event.setCancelled(true);
    Bukkit.dispatchCommand(event.getWhoClicked(), this.command);
  }
}
