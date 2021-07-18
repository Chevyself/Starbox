package me.googas.net.sockets;

import lombok.NonNull;

/** This object represents an error */
public class Error {

  /** The description on what caused the error */
  @NonNull private final String cause;

  /**
   * Create the error
   *
   * @param cause the cause of the error
   */
  public Error(@NonNull String cause) {
    this.cause = cause;
  }

  /**
   * Get the cause of the error
   *
   * @return the cause
   */
  @NonNull
  public String getCause() {
    return this.cause;
  }
}
