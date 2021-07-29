package me.googas.reflect;

import java.util.Optional;
import java.util.StringJoiner;

public class StarboxWrapper<T> implements Wrapper<T> {

  private T reference;

  public StarboxWrapper(T reference) {
    this.reference = reference;
  }

  @Override
  public Optional<T> get() {
    return Optional.ofNullable(this.reference);
  }

  @Override
  public void set(T object) {
    this.reference = object;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StarboxWrapper.class.getSimpleName() + "[", "]")
        .add("reference=" + reference)
        .toString();
  }
}
