package me.googas.starbox.modules.channels;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.starbox.BukkitLanguage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** A channel that is used to send data to a player. */
public class PlayerChannel implements Channel {

  @NonNull private final UUID uuid;

  /**
   * Start the channel.
   *
   * @param uuid the unique id of the player
   */
  protected PlayerChannel(@NonNull UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Get the unique id of the player.
   *
   * @return the unique id
   */
  @NonNull
  public UUID getUniqueId() {
    return uuid;
  }

  /**
   * Get the player.
   *
   * @return a {@link Optional} holding the nullable player
   */
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

  @Override
  public Optional<Locale> getLocale() {
    return this.getPlayer().map(BukkitLanguage::getLocale);
  }
}
