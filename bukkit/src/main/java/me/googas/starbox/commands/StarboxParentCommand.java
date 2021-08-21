package me.googas.starbox.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.starbox.BukkitLanguage;
import me.googas.starbox.builders.MapBuilder;
import org.bukkit.command.CommandSender;

/** A simple extension of {@link StarboxBukkitCommand} to easily create parent commands. */
public abstract class StarboxParentCommand extends StarboxBukkitCommand {

  @NonNull @Getter private final List<StarboxBukkitCommand> children = new ArrayList<>();

  /**
   * Create the command.
   *
   * @param name the name of the command
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   */
  public StarboxParentCommand(
      @NonNull String name, boolean async, @NonNull CommandManager manager) {
    super(name, async, manager);
  }

  /**
   * Create the command.
   *
   * @param name the name of the command
   * @param description a simple description of the command
   * @param usageMessage a message describing how the message should executed. You can learn more
   *     about usage messages in {@link Argument}
   * @param aliases the aliases which also allow to execute the command
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   */
  public StarboxParentCommand(
      @NonNull String name,
      @NonNull String description,
      @NonNull String usageMessage,
      @NonNull List<String> aliases,
      boolean async,
      @NonNull CommandManager manager) {
    super(name, description, usageMessage, aliases, async, manager);
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    BukkitLanguage language = BukkitLanguage.getLanguage(sender);
    List<StarboxBukkitCommand> children =
        this.getChildren().stream()
            .filter(
                child ->
                    child.getPermission() == null || sender.hasPermission(child.getPermission()))
            .collect(Collectors.toList());
    if (children.isEmpty()) {
      BukkitUtils.send(sender, language.get("subcommands.empty-children"));
    } else {
      BukkitUtils.send(sender, language.get("subcommands.title"));
      children.forEach(
          child ->
              BukkitUtils.send(
                  sender,
                  language.get(
                      "subcommands.child",
                      MapBuilder.of("parent", this.getName())
                          .put("children", child.getName())
                          .put("description", child.getDescription())
                          .build())));
      BukkitUtils.send(sender, language.get("subcommands.bottom"));
    }
    return null;
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String commandAlias : this.getAliases()) {
      if (alias.equalsIgnoreCase(commandAlias)) return true;
    }
    return alias.equalsIgnoreCase(this.getName());
  }
}
