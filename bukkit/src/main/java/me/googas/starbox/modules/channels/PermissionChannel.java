package me.googas.starbox.modules.channels;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents a channel which may only send data to players with an specific permission, this does
 * also include the console.
 */
public class PermissionChannel implements Channel {

  @NonNull private final String permission;

  /**
   * Create the channel.
   *
   * @param permission the permission that players in this channel may have
   */
  public PermissionChannel(@NonNull String permission) {
    this.permission = permission;
  }

  @NonNull
  private Stream<? extends Player> filter() {
    return Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.hasPermission(this.permission));
  }

  @Override
  public @NonNull PermissionChannel send(@NonNull BaseComponent... components) {
    ConsoleChannel.getInstance().send(components);
    this.filter().map(Channel::of).forEach(channel -> channel.send(components));
    return this;
  }

  @Override
  public @NonNull PermissionChannel send(@NonNull String text) {
    this.filter().map(Channel::of).forEach(channel -> channel.send(text));
    return this;
  }

  @Override
  public Optional<Locale> getLocale() {
    return Optional.empty();
  }
}
