package me.googas.reflect.wrappers.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.packet.PacketDataWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.starbox.StarboxBukkitFiles;
import me.googas.starbox.utility.Versions;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Wraps the 'IChatBaseComponent' nms class.
 */
public class WrappedChatComponent extends StarboxWrapper<Object> implements PacketDataWrapper {

  @NonNull
  private static final WrappedClass<?> CHAT_BASE_COMPONENT =
      WrappedClass.forName("net.minecraft.server." + Versions.NMS + ".IChatBaseComponent");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedChatComponent(Object reference) {
    super(reference);
  }

  /**
   * Wrap components into a chat component.
   *
   * @param components the components to wrap
   * @return the wrapped components
   */
  @NonNull
  public static WrappedChatComponent of(@NonNull BaseComponent[] components) {
    return WrappedChatComponent.of(components, WrappedChatComponent.CHAT_BASE_COMPONENT.getClazz());
  }

  /**
   * Wrap chat components from the given {@link Type} of chat component.
   *
   * @param components the components to wrap
   * @param typeOfComponent the type of the chat component
   * @return the wrapped components
   */
  @NonNull
  public static WrappedChatComponent of(
      @NonNull BaseComponent[] components, @NonNull Type typeOfComponent) {
    return new WrappedChatComponent(
        Serializer.gson.fromJson(
            StarboxBukkitFiles.Contexts.JSON.getGson().toJson(components), typeOfComponent));
  }

  @Override
  public Object getHandle() {
    return this.reference;
  }

  /**
   * Wrapper for the 'IChatBaseComponent$ChatSerializer'.
   */
  public static class Serializer {

    @NonNull
    private static final WrappedClass<?> CHAT_SERIALIZER =
        WrappedClass.forName(
            "net.minecraft.server." + Versions.NMS + ".IChatBaseComponent$ChatSerializer");

    @NonNull @Getter
    private static final Gson gson =
        Serializer.CHAT_SERIALIZER
            .getDeclaredField(Gson.class, "a")
            .get(null)
            .provide()
            .orElseGet(() -> new GsonBuilder().create());
  }
}
