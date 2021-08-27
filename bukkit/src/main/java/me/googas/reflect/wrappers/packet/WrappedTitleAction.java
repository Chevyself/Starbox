package me.googas.reflect.wrappers.packet;

import lombok.NonNull;
import me.googas.reflect.packet.PacketDataWrapper;
import me.googas.reflect.packet.PacketType;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.Starbox;

/** Wraps 'EnumTitleAction' to be used in the {@link PacketType.Play.ClientBound#TITLE}. */
public enum WrappedTitleAction implements PacketDataWrapper {
  TITLE,
  SUBTITLE,
  TIMES,
  CLEAR,
  RESET;

  @NonNull
  private static final WrappedClass<?> TITLE_ACTION =
      WrappedClass.forName(
          PacketType.Play.ClientBound.TITLE.getCanonicalName() + "$EnumTitleAction");

  @NonNull
  private static final WrappedMethod<?> VALUE_OF =
      WrappedTitleAction.TITLE_ACTION.getMethod("valueOf", String.class);

  @Override
  public Object getHandle() {
    return WrappedTitleAction.VALUE_OF
        .invoke(null, this.toString())
        .handle(Starbox::severe)
        .provide()
        .orElse(null);
  }
}
