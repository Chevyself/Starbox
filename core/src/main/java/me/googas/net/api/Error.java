package me.googas.net.api;

import lombok.Getter;
import lombok.NonNull;

/** This object represents an error */
public class Error {

  /** The description on what caused the error */
  @NonNull @Getter private final String cause;

  /**
   * Create the error
   *
   * @param cause the cause of the error
   */
  public Error(@NonNull String cause) {
    this.cause = cause;
  }
}
