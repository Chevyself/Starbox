package me.googas.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;

public class WrappedConstructor {

  @Getter private final Constructor<?> constructor;

  public WrappedConstructor(Constructor<?> constructor) {
    this.constructor = constructor;
    if (this.constructor != null) this.constructor.setAccessible(true);
  }

  public WrappedConstructor() {
    this(null);
  }

  public Object invoke(Object... args) {
    if (this.constructor != null) {
      try {
        return this.constructor.newInstance(args);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedConstructor that = (WrappedConstructor) o;
    return Objects.equals(constructor, that.constructor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(constructor);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedConstructor.class.getSimpleName() + "[", "]")
        .add("constructor=" + constructor)
        .toString();
  }
}
