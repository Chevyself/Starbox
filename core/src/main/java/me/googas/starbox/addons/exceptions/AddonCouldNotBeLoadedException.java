package me.googas.starbox.addons.exceptions;

import lombok.NonNull;

/** Thrown when an {@link me.googas.starbox.addons.Addon} cannot be loaded */
public class AddonCouldNotBeLoadedException extends Exception {

  public AddonCouldNotBeLoadedException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public AddonCouldNotBeLoadedException(@NonNull String message) {
    super(message);
  }
}
