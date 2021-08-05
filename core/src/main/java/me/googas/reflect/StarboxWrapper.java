package me.googas.reflect;

import java.util.Optional;
import java.util.StringJoiner;

/**
 * An implementation for wrapper.
 *
 * @param <T> the type of object that his wrapper holds
 */
public class StarboxWrapper<T> implements Wrapper<T> {

  private T reference;

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public StarboxWrapper(T reference) {
    this.reference = reference;
  }

  @Override
  public Optional<T> get() {
    return Optional.ofNullable(this.reference);
  }

  @Override
  public void set(T object) {
    // TODO builder setter
    this.reference = object;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StarboxWrapper.class.getSimpleName() + "[", "]")
        .add("reference=" + reference)
        .toString();
  }
}
