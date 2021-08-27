package me.googas.reflect;

import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;

/**
 * An implementation for wrapper.
 *
 * @param <T> the type of object that his wrapper holds
 */
public class StarboxWrapper<T> implements Wrapper<T> {

  protected T reference;

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
  @NonNull
  public StarboxWrapper<T> set(T object) {
    this.reference = object;
    return this;
  }

  /**
   * Get whether the reference is not null.
   *
   * @return true if the reference is not null
   */
  public boolean isPresent() {
    return this.reference != null;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StarboxWrapper.class.getSimpleName() + "[", "]")
        .add("reference=" + reference)
        .toString();
  }
}
