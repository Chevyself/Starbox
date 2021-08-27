package me.googas.reflect.wrappers.entity;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.packet.Packet;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;

/**
 * Wraps the 'PlayerConnection' nms class.
 */
public class WrappedPlayerConnection extends StarboxWrapper<Object> {

  @NonNull
  private static final WrappedClass<?> ENTITY_PLAYER =
      WrappedClass.forName("net.minecraft.server." + Versions.NMS + ".PlayerConnection");

  @NonNull
  private static final WrappedMethod<?> SEND_PACKET =
          WrappedPlayerConnection.ENTITY_PLAYER.getMethod("sendPacket", Packet.PACKET_CLASS.getClazz());

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedPlayerConnection(Object reference) {
    super(reference);
  }

  /**
   * Send a packet to this client connection.
   *
   * @param packet the packet to be send
   */
  public void sendPacket(@NonNull Packet packet) {
    WrappedPlayerConnection.SEND_PACKET
        .prepare(this.reference, packet.get().orElse(null))
        .handle(Starbox::severe)
        .run();
  }
}
