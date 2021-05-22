package me.googas.starbox;

import lombok.NonNull;

/**
 * This object represents an object that will provide the given type by building it
 *
 * @param <T> the type of object to format
 */
public interface Builder<T> {

  /**
   * Build the requested type object
   *
   * @return the built object
   */
  @NonNull
  T build();
}
