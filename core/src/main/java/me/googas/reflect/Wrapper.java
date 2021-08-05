package me.googas.reflect;

import java.util.Optional;
import lombok.NonNull;

/**
 * Wraps an element.
 *
 * @param <T> the type of element to be wrapped
 */
public interface Wrapper<T> {

  /**
   * Get the wrapped object.
   *
   * @return a {@link Optional} containing the nullable wrapped object
   */
  @NonNull
  Optional<T> get();

  /**
   * Set the wrapped object.
   *
   * @param object the new wrapped object
   */
  void set(T object);
}
