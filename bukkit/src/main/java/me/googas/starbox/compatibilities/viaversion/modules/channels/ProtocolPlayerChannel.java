package me.googas.starbox.compatibilities.viaversion.modules.channels;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.starbox.modules.channels.PlayerChannel;
import me.googas.starbox.utility.Versions;

/**
 * Represents a {@link PlayerChannel} which methods may change due to being in a different protocol
 * version.
 */
public class ProtocolPlayerChannel extends PlayerChannel {

  @NonNull @Getter @Setter private Versions.Player version;

  /**
   * Start the channel.
   *
   * @param uuid the unique id of the player
   */
  protected ProtocolPlayerChannel(@NonNull UUID uuid) {
    super(uuid);
  }
}
