package me.googas.starbox.reflect.wrappers;

import java.util.StringJoiner;

public class SimpleWrapper implements Wrapper {

  private Object reference;

  public SimpleWrapper(Object reference) {
    this.reference = reference;
  }

  @Override
  public Object get() {
    return this.reference;
  }

  @Override
  public void set(Object object) {
    this.reference = object;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", SimpleWrapper.class.getSimpleName() + "[", "]")
        .add("reference=" + reference)
        .toString();
  }
}
