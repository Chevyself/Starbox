package me.googas.reflect.wrappers;

import java.util.Objects;
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
  @NonNull
  public LangWrapper<T> set(T object) {
    throw new UnsupportedOperationException("References in LangWrappers are final");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    LangWrapper<?> that = (LangWrapper<?>) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }
}
