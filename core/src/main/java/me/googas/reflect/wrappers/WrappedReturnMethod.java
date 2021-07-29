package me.googas.reflect.wrappers;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;

/**
 * This class wraps a {@link Method} to invoke and return an object
 *
 * @param <T> the type of object that will return when invoked
 */
public class WrappedReturnMethod<T> extends WrappedMethod {

  private final Class<T> returnType;

  private WrappedReturnMethod(Method method, Class<T> returnType) {
    super(method);
    this.returnType = returnType;
  }

  WrappedReturnMethod() {
    this(null, null);
  }

  /**
   * Wrap a {@link Method} instance
   *
   * @param method the method to wrap
   * @param returnType the type to return when the method is invoked
   * @return the wrapper of the method
   */
  @NonNull
  public static <T> WrappedReturnMethod<T> of(Method method, Class<T> returnType) {
    if (method != null) method.setAccessible(true);
    return new WrappedReturnMethod<>(method, returnType);
  }

  @NonNull
  @Deprecated
  public T invokeOr(@NonNull Object object, @NonNull T def, @NonNull Object... params) {
    return this.invoke(object, params).orElse(def);
  }

  @Override
  public @NonNull Optional<Class<?>> getReturnType() {
    return Optional.ofNullable(this.returnType);
  }

  @Override
  public Optional<T> invoke(Object object, @NonNull Object... params) {
    T t = null;
    if (this.returnType != null) {
      Object invoked = super.invoke(object, params);
      if (invoked != null && this.returnType.isAssignableFrom(invoked.getClass())) {
        this.returnType.cast(t);
      }
    }
    return Optional.ofNullable(t);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    WrappedReturnMethod<?> that = (WrappedReturnMethod<?>) o;
    return Objects.equals(returnType, that.returnType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), returnType);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedReturnMethod.class.getSimpleName() + "[", "]")
        .add("returnType=" + returnType)
        .toString();
  }
}
