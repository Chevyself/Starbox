package me.googas.reflect.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.starbox.expressions.HandledExpression;

/** This class wraps a {@link Method} to prepare */
public class WrappedMethod<T> extends LangWrapper<Method> {

  private final Class<T> returnType;

  protected WrappedMethod(Method reference, Class<T> returnType) {
    super(reference);
    this.returnType = returnType;
  }

  public WrappedMethod() {
    this(null, null);
  }

  /**
   * Wrap a {@link Method} instance
   *
   * @param method the method to wrap
   * @return the wrapper of the method
   */
  @NonNull
  public static <T> WrappedMethod<T> of(Method method) {
    if (method != null) method.setAccessible(true);
    return new WrappedMethod<>(method, null);
  }

  @NonNull
  public static <T> WrappedMethod<T> of(Method method, @NonNull Class<T> returnType) {
    if (method != null) method.setAccessible(true);
    return new WrappedMethod<>(method, returnType);
  }

  /**
   * Invoke the method
   *
   * @param object the instance of the object to prepare the method if the method is static it may
   *     be null
   * @param params the parameters to prepare the method
   * @return a {@link HandledExpression} returning the object which the method returns and handles
   *     {@link IllegalAccessException} {@link InvocationTargetException} and {@link
   *     ClassCastException} in case the return type does not match {@link #returnType}
   */
  @NonNull
  public HandledExpression<T> prepare(Object object, Object... params) {
    return HandledExpression.using(
        () -> {
          T obj = null;
          if (this.reference != null) {
            Object invoke = this.reference.invoke(object, params);
            if (invoke != null) obj = returnType.cast(invoke);
          }
          return obj;
        });
  }

  /**
   * The type that the method returns when {@link #prepare(Object, Object...)}
   *
   * @return an {@link Optional} instance holding the return type
   */
  @NonNull
  public Optional<Class<?>> getReturnType() {
    return Optional.ofNullable(this.returnType);
  }

  /**
   * Get the wrapped method
   *
   * @return the wrapped method instance
   */
  public Method getMethod() {
    return this.reference;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedMethod.class.getSimpleName() + "[", "]")
        .add("method=" + reference)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    WrappedMethod<?> that = (WrappedMethod<?>) o;
    return Objects.equals(returnType, that.returnType) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), returnType) + super.hashCode();
  }
}
