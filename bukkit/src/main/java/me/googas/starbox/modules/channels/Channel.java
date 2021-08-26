package me.googas.starbox.modules.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import me.googas.starbox.Starbox;
import me.googas.starbox.compatibilities.viaversion.modules.channels.ProtocolChannelsModule;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** A channel to send different type of data. */
public interface Channel {

  @NonNull List<PlayerChannel> players = new ArrayList<>();

  /**
   * Get the channel of a {@link CommandSender}.
   *
   * @param sender the sender to get the channel from
   * @return the channel of the sender
   */
  @NonNull
  static Channel of(@NonNull CommandSender sender) {
    if (sender instanceof Player) {
      return Channel.of((Player) sender);
    } else {
      return ConsoleChannel.getInstance();
    }
  }

  /**
   * Get the channel of a {@link Player}.
   *
   * @param player the player to get the channel from
   * @return the channel
   */
  static PlayerChannel of(@NonNull Player player) {
    return Channel.of(player.getUniqueId());
  }

  /**
   * Get the channel of a {@link Player} based on its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the channel of the player
   */
  static PlayerChannel of(@NonNull UUID uniqueId) {
    return new ArrayList<>(ConsoleChannel.players)
        .stream()
            .filter(channel -> channel.getUniqueId().equals(uniqueId))
            .findFirst()
            .orElseGet(
                () -> {
                  PlayerChannel channel;
                  if (Starbox.getCompatibilities().isEnabled("ViaVersion")) {
                    channel =
                        Starbox.getModules()
                            .get(ProtocolChannelsModule.class)
                            .map(module -> module.getChannel(uniqueId))
                            .orElseGet(() -> new PlayerChannel(uniqueId));
                  } else {
                    channel = new PlayerChannel(uniqueId);
                  }
                  Channel.players.add(channel);
                  return channel;
                });
  }

  /**
   * Send base components to the channel.
   *
   * @param components the components to send
   * @return this same instance
   */
  @NonNull
  Channel send(@NonNull BaseComponent... components);

  /**
   * Send a text message to the channel.
   *
   * @param text the text message to send
   * @return this same instance
   */
  @NonNull
  Channel send(@NonNull String text);

  /**
   * Send a localized {@link BukkitLine}.
   *
   * @param key the key to match the json/text
   * @return this same instance
   */
  @NonNull
  default Channel localized(@NonNull String key) {
    this.send(BukkitLine.localized(this, key).build());
    return this;
  }

  /**
   * Send a localized formatted {@link BukkitLine}.
   *
   * @see BukkitLine#format(Map)
   * @param key the key to match the json/text
   * @param map to format the line
   * @return this same instance
   */
  @NonNull
  default Channel localized(@NonNull String key, @NonNull Map<String, String> map) {
    this.send(BukkitLine.localized(this, key).format(map).build());
    return this;
  }

  /**
   * Send a localized formatted {@link BukkitLine}.
   *
   * @see BukkitLine#format(Object...)
   * @param key the key to match the json/text
   * @param objects to format the line
   * @return this same instance
   */
  @NonNull
  default Channel localized(@NonNull String key, @NonNull Object... objects) {
    this.send(BukkitLine.localized(this, key).format(objects).build());
    return this;
  }

  /**
   * Get the locale of the channel.
   *
   * @return the locale
   */
  Optional<Locale> getLocale();
}
