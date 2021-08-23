package me.googas.starbox;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.commands.ComponentBuilderCommands;
import me.googas.starbox.commands.ItemBuilderCommands;
import me.googas.starbox.commands.providers.BungeeChatColorProvider;
import me.googas.starbox.commands.providers.ChannelProvider;
import me.googas.starbox.commands.providers.ClickEventActionProvider;
import me.googas.starbox.commands.providers.EnchantmentProvider;
import me.googas.starbox.commands.providers.FormatRetentionProvider;
import me.googas.starbox.commands.providers.HoverEventActionProvider;
import me.googas.starbox.modules.ModuleRegistry;
import me.googas.starbox.modules.language.LanguageModule;
import me.googas.starbox.modules.ui.UIModule;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class of the Starbox Bukkit plugin. */
public class StarboxPlugin extends JavaPlugin {

  @NonNull @Getter private final ModuleRegistry modules = new ModuleRegistry(this);

  @Override
  public void onEnable() {
    Starbox.setInstance(this);
    modules.engage(
        new UIModule(),
        new LanguageModule().register(this, BukkitYamlLanguage.of(this, "language")));
    BukkitMessagesProvider messagesProvider = new BukkitMessagesProvider();
    ProvidersRegistry<CommandContext> registry =
        new BukkitProvidersRegistry(messagesProvider)
            .addProviders(
                new BungeeChatColorProvider(),
                new ChannelProvider(),
                new ClickEventActionProvider(),
                new EnchantmentProvider(),
                new FormatRetentionProvider(),
                new HoverEventActionProvider());
    CommandManager manager =
        new CommandManager(this, registry, messagesProvider).registerHelpFactory();
    ComponentBuilderCommands.Parent componentBuilder = new ComponentBuilderCommands.Parent(manager);
    ItemBuilderCommands.Parent itemBuilder = new ItemBuilderCommands.Parent(manager);
    componentBuilder.getChildren().addAll(manager.parseCommands(new ComponentBuilderCommands()));
    itemBuilder.getChildren().addAll(manager.parseCommands(new ItemBuilderCommands()));
    manager.registerAll(componentBuilder, itemBuilder);
    manager.registerPlugin();
    super.onEnable();
  }

  @Override
  public void onDisable() {
    modules.disengageAll();
    super.onDisable();
  }
}
