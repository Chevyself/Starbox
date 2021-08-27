package me.googas.reflect.packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.entity.WrappedCraftPlayer;
import me.googas.starbox.Starbox;
import me.googas.starbox.utility.Versions;
import org.bukkit.entity.Player;

/** Represents a Packet which can be send to a player or may be send by a player. */
public class Packet extends StarboxWrapper<Object> {

  @NonNull
  public static final WrappedClass<?> PACKET_CLASS =
      WrappedClass.forName("net.minecraft.server." + Versions.NMS + ".Packet");

  @NonNull @Getter private final PacketType type;
  @NonNull @Getter private final WrappedClass<?> clazz;

  /**
   * Create the packet.
   *
   * @param type the type of the packet
   * @param clazz the class of the type of the packet
   * @param reference the created reference of the packet
   */
  public Packet(@NonNull PacketType type, @NonNull WrappedClass<?> clazz, Object reference) {
    super(reference);
    this.type = type;
    this.clazz = clazz;
  }

  /**
   * Create a packet for a given packet type.
   *
   * @param type the type of the packet to create
   * @return the created packet
   */
  @NonNull
  public static Packet forType(@NonNull PacketType type) {
    WrappedClass<?> clazz = type.wrap();
    Object handle = clazz.getConstructor().invoke().handle(Starbox::severe).provide().orElse(null);
    return new Packet(type, clazz, handle);
  }

  /**
   * Edit the packet using its class.
   *
   * @param consumer the consumer to edit the packet
   * @return this same instance
   */
  @NonNull
  public Packet usingClazz(@NonNull BiConsumer<Object, WrappedClass<?>> consumer) {
    consumer.accept(this.reference, this.clazz);
    return this;
  }

  /**
   * Set a field in the packet.
   *
   * @param index the index of the field
   * @param packetData the packet data to set in the field
   * @return this same instance
   */
  @NonNull
  public Packet setField(int index, PacketDataWrapper packetData) {
    return this.setField(index, packetData == null ? null : packetData.getHandle());
  }

  /**
   * Set a field in the packet.
   *
   * @param index the index of the field
   * @param object the object to set in the field
   * @return this same instance
   */
  @NonNull
  public Packet setField(int index, Object object) {
    this.clazz
        .getDeclaredFields()
        .get(index)
        .set(this.reference, object)
        .handle(Starbox::severe)
        .run();
    return this;
  }

  /**
   * Send this packet to a player.
   *
   * @param player the player to send this packet to
   */
  public void send(@NonNull Player player) {
    WrappedCraftPlayer.of(player).getHandle().playerConnection().sendPacket(this);
  }

  /**
   * Send this packet to many players.
   *
   * @param players the collection of players to send this packet to
   */
  public void sendAll(@NonNull Collection<? extends Player> players) {
    players.forEach(this::send);
  }

  /**
   * Send this packet to many players.
   *
   * @param players the array of players to send this packet to
   */
  public void sendAll(@NonNull Player... players) {
    this.sendAll(Arrays.asList(players));
  }
}
