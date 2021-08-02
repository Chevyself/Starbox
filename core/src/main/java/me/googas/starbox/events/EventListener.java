package me.googas.starbox.events;

import java.lang.reflect.Method;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

/**
 * This class represents each method that is listening to an event. {@link #listener} is the object
 * from which the {@link #method} will be invoked. The method includes the only parameter that is
 * the {@link #event} that it is listening to
 */
public class EventListener {

  @NonNull @Getter private final Object listener;
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Class<? extends Event> event;
  @Getter private final int priority;

  /**
   * Create an event listener instance which can be used inside the {@link ListenerManager} to be
   * called
   *
   * @param listener the object used to prepare the method
   * @param method the method that is listening to the event
   * @param event the event that is being listened to. Required to prepare the method
   * @param priority the priority for the event to be called in the listener
   */
  public EventListener(
      @NonNull Object listener,
      @NonNull Method method,
      @NonNull Class<? extends Event> event,
      int priority) {
    this.listener = listener;
    this.method = method;
    this.event = event;
    this.priority = priority;
  }

  public String toString() {
    return new StringJoiner(", ", EventListener.class.getSimpleName() + "[", "]")
        .add("listener=" + listener)
        .add("method=" + method)
        .add("event=" + event)
        .add("priority=" + priority)
        .toString();
  }
}
