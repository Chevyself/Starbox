package me.googas.reflect.wrappers.entity;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedField;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;

/**
 * Wraps the 'EntityPlayer' nms class.
 */
public class WrappedEntityPlayer extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> ENTITY_PLAYER =
      WrappedClass.forName("net.minecraft.server." + Versions.NMS + ".EntityPlayer");

  @NonNull
  private static final WrappedField<?> PLAYER_CONNECTION =
      WrappedEntityPlayer.ENTITY_PLAYER.getDeclaredField("playerConnection");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedEntityPlayer(Object reference) {
    super(reference);
  }

  /**
   * Get the wrapped player connection of this player.
   *
   * @return the wrapped player connection
   * @throws NullPointerException if the connection could not be provided
   */
  @NonNull
  public WrappedPlayerConnection playerConnection() {
    return new WrappedPlayerConnection(
        WrappedEntityPlayer.PLAYER_CONNECTION
            .provide(reference)
            .handle(Starbox::severe)
            .provide()
            .orElseThrow(() -> new NullPointerException("Could not get PlayerConnection")));
  }
}
