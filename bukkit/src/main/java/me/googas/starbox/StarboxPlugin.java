package me.googas.starbox;

import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.starbox.commands.ItemBuilderCommands;
import me.googas.starbox.modules.ModuleRegistry;
import me.googas.starbox.modules.ui.UIModule;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class of the Starbox Bukkit plugin. */
public class StarboxPlugin extends JavaPlugin {

  @NonNull
  private final ModuleRegistry modules = new ModuleRegistry(this);

  @Override
  public void onEnable() {
    Starbox.setInstance(this);
    modules.engage(new UIModule());
    BukkitMessagesProvider messagesProvider = new BukkitMessagesProvider();
    CommandManager manager =
        new CommandManager(this, new BukkitProvidersRegistry(messagesProvider), messagesProvider)
            .parseAndRegister(new ItemBuilderCommands());
    manager.registerPlugin();
    super.onEnable();
  }

  @Override
  public void onDisable() {
    modules.disengageAll();
    super.onDisable();
  }
}
