package me.googas.starbox.modules.channels;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.utils.BukkitUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * A channel that is used to send data to a player.
 */
public class PlayerChannel implements Channel {

  @NonNull
  private final UUID uuid;

  protected PlayerChannel(@NonNull UUID uuid) {
    this.uuid = uuid;
  }

  @NonNull
  public UUID getUniqueId() {
    return uuid;
  }

  @NonNull
  public Optional<Player> getPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(uuid));
  }

  @Override
  @NonNull
  public PlayerChannel send(@NonNull BaseComponent... components) {
    this.getPlayer().ifPresent(player -> BukkitUtils.send(player, components));
    return this;
  }

  @Override
  @NonNull
  public PlayerChannel send(@NonNull String text) {
    this.getPlayer().ifPresent(player -> player.sendMessage(text));
    return this;
  }
}
