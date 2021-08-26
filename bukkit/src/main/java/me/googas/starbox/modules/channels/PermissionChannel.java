package me.googas.starbox.modules.channels;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents a channel which may only send data to players with an specific permission, this does
 * also include the console.
 */
public class PermissionChannel implements ForwardingChannel.Multiple {

  @NonNull private final String permission;

  /**
   * Create the channel.
   *
   * @param permission the permission that players in this channel may have
   */
  public PermissionChannel(@NonNull String permission) {
    this.permission = permission;
  }

  /**
   * Filter all the players with the channel permission.
   *
   * @return the stream of filtered players
   */
  @NonNull
  public Stream<? extends Player> filter() {
    return Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.hasPermission(this.permission));
  }

  @Override
  public @NonNull List<Channel> getChannels() {
    List<Channel> channels = this.filter().map(Channel::of).collect(Collectors.toList());
    channels.add(ConsoleChannel.getInstance());
    return channels;
  }

  @Override
  public Optional<Locale> getLocale() {
    return Optional.empty();
  }
}
