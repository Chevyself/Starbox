package me.googas.starbox;

import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.commands.ComponentBuilderCommands;
import me.googas.starbox.commands.ItemBuilderCommands;
import me.googas.starbox.commands.providers.BungeeChatColorProvider;
import me.googas.starbox.commands.providers.ClickEventActionProvider;
import me.googas.starbox.commands.providers.FormatRetentionProvider;
import me.googas.starbox.commands.providers.HoverEvenActionProvider;
import me.googas.starbox.modules.ModuleRegistry;
import me.googas.starbox.modules.ui.UIModule;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class of the Starbox Bukkit plugin. */
public class StarboxPlugin extends JavaPlugin {

  @NonNull private final ModuleRegistry modules = new ModuleRegistry(this);

  @Override
  public void onEnable() {
    Starbox.setInstance(this);
    modules.engage(new UIModule());
    BukkitMessagesProvider messagesProvider = new BukkitMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new BukkitProvidersRegistry(messagesProvider)
            .addProviders(
                new BungeeChatColorProvider(),
                new ClickEventActionProvider(),
                new FormatRetentionProvider(),
                new HoverEvenActionProvider());
    CommandManager manager =
        new CommandManager(this, registry, messagesProvider)
            .parseAndRegisterAll(new ComponentBuilderCommands(), new ItemBuilderCommands());
    manager.registerPlugin();
    super.onEnable();
  }

  @Override
  public void onDisable() {
    modules.disengageAll();
    super.onDisable();
  }
}
