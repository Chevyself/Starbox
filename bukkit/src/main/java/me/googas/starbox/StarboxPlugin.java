package me.googas.starbox;

import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.starbox.commands.ItemBuilderCommands;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class of the Starbox Bukkit plugin. */
public class StarboxPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    Starbox.setInstance(this);
    BukkitMessagesProvider messagesProvider = new BukkitMessagesProvider();
    CommandManager manager =
        new CommandManager(this, new BukkitProvidersRegistry(messagesProvider), messagesProvider)
            .parseAndRegister(new ItemBuilderCommands());
    manager.registerPlugin();
  }
}
