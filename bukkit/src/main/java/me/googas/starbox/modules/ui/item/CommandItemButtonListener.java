package me.googas.starbox.modules.ui.item;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;

/** This type of listener is used for when a player clicks a button it will run a command. */
public class CommandItemButtonListener implements ItemButtonListener {

  @NonNull private final String command;

  /**
   * Create the listener.
   *
   * @param command the command to execute when clicked
   */
  public CommandItemButtonListener(@NonNull String command) {
    this.command = command;
  }

  @Override
  public void onClick(@NonNull PlayerInteractEvent event) {
    event.setCancelled(true);
    Bukkit.dispatchCommand(event.getPlayer(), command);
  }
}
