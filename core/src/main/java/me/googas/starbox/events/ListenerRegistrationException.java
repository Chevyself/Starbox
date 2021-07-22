package me.googas.starbox.events;

import lombok.NonNull;

/**
 * Thrown when a listener cannot be registered in {@link ListenerManager#registerListeners(Object)}
 */
public class ListenerRegistrationException extends RuntimeException {

  /**
   * Throw a simple runtime exception with a message
   *
   * @param message the message
   */
  public ListenerRegistrationException(@NonNull String message) {
    super(message);
  }
}
