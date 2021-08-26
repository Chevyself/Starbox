package me.googas.starbox.compatibilities.viaversion.modules.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import me.googas.starbox.modules.Module;
import me.googas.starbox.modules.channels.Channel;
import me.googas.starbox.modules.channels.PlayerChannel;
import me.googas.starbox.utility.Versions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.myles.ViaVersion.api.Via;

/** Creates channels based on protocol version. */
public class ProtocolChannelsModule implements Module {

  @NonNull private final Map<UUID, Versions.Player> versions = new HashMap<>();

  /**
   * Get the channel for a {@link Player} with its {@link UUID}.
   *
   * @param uniqueId the unique id of the player
   * @return the created channel for the player
   */
  @NonNull
  public PlayerChannel getChannel(@NonNull UUID uniqueId) {
    return new ProtocolPlayerChannel(uniqueId);
  }

  /**
   * Get the version of the player.
   *
   * @param player the player to get the version
   * @return the version of the player
   */
  @SuppressWarnings("deprecation")
  @NonNull
  public Versions.Player getVersion(@NonNull Player player) {
    return versions.computeIfAbsent(
        player.getUniqueId(),
        // This VIA api is deprecated and will be removed in future versions we are using it until
        // via version drops support for it fully
        uuid -> Versions.getVersion(Via.getAPI().getPlayerVersion(player.getUniqueId())));
  }

  /**
   * Remove the player version from the map when the player quits the game.
   *
   * @param event the event of the player quiting
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event) {
    versions.remove(event.getPlayer().getUniqueId());
  }

  /**
   * Add the version of the player to the map and set the version on its {@link
   * ProtocolPlayerChannel}.
   *
   * @param event the event of a player joining the game
   */
  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PlayerChannel playerChannel = Channel.of(player);
    if (playerChannel instanceof ProtocolPlayerChannel)
      ((ProtocolPlayerChannel) playerChannel).setVersion(this.getVersion(player));
  }

  @Override
  public @NonNull String getName() {
    return "protocol-channels";
  }
}
