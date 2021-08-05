package me.googas.starbox.addons.exceptions;

import lombok.NonNull;

/** Thrown when an {@link me.googas.starbox.addons.Addon} cannot be loaded. */
public class AddonCouldNotBeLoadedException extends Exception {

  /**
   * Create the exception.
   *
   * @param message the message explaining why it cannot be loaded
   * @param cause the exception that caused that the addon could not be loaded
   */
  public AddonCouldNotBeLoadedException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Create the exception.
   *
   * @param message the message explaining why it cannot be loaded
   */
  public AddonCouldNotBeLoadedException(@NonNull String message) {
    super(message);
  }
}
