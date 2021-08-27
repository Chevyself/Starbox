package me.googas.reflect.wrappers.entity;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;
import org.bukkit.entity.Player;

/** Wraps the 'CraftPlayer' nms class. */
public class WrappedCraftPlayer extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> CRAFT_PLAYER =
      WrappedClass.forName("org.bukkit.craftbukkit." + Versions.NMS + ".entity.CraftPlayer");

  @NonNull
  private static final WrappedMethod<?> GET_PROFILE =
      WrappedCraftPlayer.CRAFT_PLAYER.getMethod("getProfile");

  @NonNull
  private static final WrappedMethod<?> GET_HANDLE =
      WrappedCraftPlayer.CRAFT_PLAYER.getMethod("getHandle");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  private WrappedCraftPlayer(Object reference) {
    super(reference);
  }

  /**
   * Wrap a player into its 'CraftPlayer'.
   *
   * @param player the player to wrap
   * @return the wrapped craft player
   */
  @NonNull
  public static WrappedCraftPlayer of(@NonNull Player player) {
    return new WrappedCraftPlayer(player);
  }

  /**
   * Get the wrapped entity player of this craft player.
   *
   * @return the wrapped entity player
   */
  @NonNull
  public WrappedEntityPlayer getHandle() {
    return new WrappedEntityPlayer(
        WrappedCraftPlayer.GET_HANDLE
            .invoke(reference)
            .handle(Starbox::severe)
            .provide()
            .orElseThrow(() -> new NullPointerException("Could not get EntityPlayer")));
  }

  /**
   * Get the wrapped game profile of the player.
   *
   * @return the wrapped game profile
   */
  public WrappedGameProfile getProfile() {
    return new WrappedGameProfile(
        WrappedCraftPlayer.GET_PROFILE
            .invoke(reference)
            .handle(Starbox::severe)
            .provide()
            .orElseThrow(IllegalStateException::new));
  }
}
