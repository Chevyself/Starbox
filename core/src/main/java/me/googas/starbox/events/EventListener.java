package me.googas.starbox.events;

import lombok.NonNull;

/**
 * This object listens for {@link Event}. Create implementations that can be registered in {@link
 * ListenerManager} without reflection
 */
public interface EventListener {

  /**
   * Handle an event.
   *
   * @param event the event which is being called
   */
  void call(@NonNull Event event);

  /**
   * Get the priority of the listener. The priority represents the order in which listeners are
   * called thus listeners with a lower priority get called first giving listeners with high
   * priority make the final decisions of a listener
   *
   * @return the priority of the listener
   */
  default int getPriority() {
    return 1;
  }
}
