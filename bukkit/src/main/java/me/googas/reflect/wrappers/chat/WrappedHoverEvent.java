package me.googas.reflect.wrappers.chat;

import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedConstructor;
import me.googas.starbox.Starbox;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;

/** Class to wrap {@link HoverEvent} to not crash when older versions cannot use it. */
public class WrappedHoverEvent extends StarboxWrapper<HoverEvent> {

  @NonNull
  private static final WrappedClass<HoverEvent> HOVER_EVENT = WrappedClass.of(HoverEvent.class);

  @NonNull
  private static final WrappedConstructor<HoverEvent> HOVER_EVENT_CONSTRUCTOR =
      WrappedHoverEvent.HOVER_EVENT.getConstructor(HoverEvent.Action.class, BaseComponent[].class);

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedHoverEvent(HoverEvent reference) {
    super(reference);
  }

  /**
   * Construct a hover event.
   *
   * @param action the action to run at hover
   * @param components the components to show at hover
   * @return the wrapped hover event
   * @throws IllegalArgumentException if hover event could not be created
   */
  @NonNull
  @APIVersion(since = 8, max = 15)
  public static WrappedHoverEvent construct(
      @NonNull HoverEvent.Action action, @NonNull BaseComponent[] components) {
    return new WrappedHoverEvent(
        WrappedHoverEvent.HOVER_EVENT_CONSTRUCTOR
            .invoke(action, components)
            .handle(Starbox::severe)
            .provide()
            .orElseThrow(() -> new IllegalArgumentException("Could not create HoverEvent")));
  }

  /**
   * Construct a hover event.
   *
   * @param action the action to run at hover
   * @param content the content to show at hover
   * @return the wrapped hover event
   */
  @NonNull
  @APIVersion(since = 16)
  public static WrappedHoverEvent construct(
      @NonNull HoverEvent.Action action, @NonNull WrappedContent<?> content) {
    return new WrappedHoverEvent(
        new HoverEvent(action, content.get().orElseThrow(NullPointerException::new)));
  }

  /**
   * Get the event validated that is not null.
   *
   * @return the event
   * @throws NullPointerException if the event is null
   */
  @NonNull
  public HoverEvent getEvent() {
    return this.get()
        .orElseThrow(() -> new NullPointerException("Event was not constructed correctly"));
  }
}
