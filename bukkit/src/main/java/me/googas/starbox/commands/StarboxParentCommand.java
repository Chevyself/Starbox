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
import me.googas.starbox.builders.MapBuilder;
import me.googas.starbox.modules.channels.Channel;

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
    Channel channel = Channel.of(context.getSender());
    List<StarboxBukkitCommand> children =
        this.getChildren().stream()
            .filter(
                child ->
                    child.getPermission() == null
                        || context.getSender().hasPermission(child.getPermission()))
            .collect(Collectors.toList());
    if (children.isEmpty()) {
      channel.localized("subcommands.empty-children");
    } else {
      channel.localized("subcommands.title");
      children.forEach(
          child ->
              channel.localized(
                  "subcommands.child",
                  MapBuilder.of("parent", this.getName())
                      .put("children", child.getName())
                      .put("description", child.getDescription())
                      .build()));
      channel.localized("subcommands.bottom");
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
