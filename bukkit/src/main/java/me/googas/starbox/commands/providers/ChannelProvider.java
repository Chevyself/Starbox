package me.googas.starbox.commands.providers;

import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import me.googas.starbox.modules.channels.Channel;

/** Provides {@link Channel} to the {@link me.googas.commands.bukkit.CommandManager}. */
public class ChannelProvider implements BukkitExtraArgumentProvider<Channel> {

  @Override
  public @NonNull Class<Channel> getClazz() {
    return Channel.class;
  }

  @Override
  public @NonNull Channel getObject(@NonNull CommandContext context) {
    return Channel.of(context.getSender());
  }
}
