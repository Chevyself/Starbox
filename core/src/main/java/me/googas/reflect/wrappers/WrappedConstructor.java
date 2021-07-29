package me.googas.reflect.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;

/** This class wraps a {@link Constructor} to invoke it and create instances of a class */
public class WrappedConstructor extends LangWrapper<Constructor<?>> {

  private WrappedConstructor(Constructor<?> reference) {
    super(reference);
  }

  WrappedConstructor() {
    this(null);
  }

  /**
   * Wrap a {@link Constructor} instance
   *
   * @param constructor the constructor to wrap
   * @return the wrapper of constructor
   */
  @NonNull
  public static WrappedConstructor of(Constructor<?> constructor) {
    if (constructor != null) constructor.setAccessible(true);
    return new WrappedConstructor(constructor);
  }

  /**
   * Invoke the constructor to create a new instance of an object
   *
   * @param args the required arguments to invoke the constructor
   * @return the created object or null if an error was thrown
   */
  public Object invoke(Object... args) {
    if (this.reference != null) {
      try {
        return this.reference.newInstance(args);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Get the wrapped constructor
   *
   * @return the wrapped constructor
   */
  public Constructor<?> getConstructor() {
    return reference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedConstructor that = (WrappedConstructor) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedConstructor.class.getSimpleName() + "[", "]")
        .add("constructor=" + reference)
        .toString();
  }
}
