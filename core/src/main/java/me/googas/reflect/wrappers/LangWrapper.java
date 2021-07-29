package me.googas.reflect.wrappers;

import java.util.Optional;
import lombok.NonNull;
import me.googas.reflect.Wrapper;

/**
 * Implementation of wrapper for classes located in the Java package 'java.lang.reflect'
 *
 * @param <T> the wrapped type
 */
public class LangWrapper<T> implements Wrapper<T> {

  @NonNull final T reference;

  LangWrapper(T reference) {
    this.reference = reference;
  }

  @Override
  public @NonNull Optional<T> get() {
    return Optional.ofNullable(this.reference);
  }

  @Override
  public void set(T object) {
    throw new UnsupportedOperationException("References in LangWrappers are final");
  }
}
