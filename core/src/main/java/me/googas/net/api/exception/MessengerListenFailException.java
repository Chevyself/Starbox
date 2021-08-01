package me.googas.net.api.exception;

import lombok.NonNull;
import me.googas.net.api.Messenger;

/** Thrown when the {@link Messenger} fails to {@link Messenger#listen()} */
public class MessengerListenFailException extends Exception {

  /**
   * Create the exception
   *
   * @param message the message to why it failed
   */
  public MessengerListenFailException(String message) {
    super(message);
  }

  /**
   * Create the exception
   *
   * @param message the message to why it failed
   * @param cause the throwable that made if fail
   */
  public MessengerListenFailException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}
