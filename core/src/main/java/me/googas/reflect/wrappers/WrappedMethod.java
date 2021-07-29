package me.googas.reflect.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;

/** This class wraps a {@link Method} to invoke */
public class WrappedMethod extends LangWrapper<Method> {

  protected WrappedMethod(Method reference) {
    super(reference);
  }

  public WrappedMethod() {
    this(null);
  }

  /**
   * Wrap a {@link Method} instance
   *
   * @param method the method to wrap
   * @return the wrapper of the method
   */
  @NonNull
  public static WrappedMethod of(Method method) {
    if (method != null) method.setAccessible(true);
    return new WrappedMethod(method);
  }

  /**
   * Wrap a method finding it from a class and its parameters
   *
   * @param clazz the class to get the method from
   * @param name the name to match the method
   * @param params the parameters to match the method
   * @return a wrapper of the method
   */
  @NonNull
  public static WrappedMethod of(
      @NonNull Class<?> clazz, @NonNull String name, @NonNull Class<?>... params) {
    return WrappedClass.of(clazz).getMethod(name, params);
  }

  /**
   * Wrap a method finding it from a class which will be found using the qualified name
   *
   * @param className the fully qualified name of the class
   * @param name the name to match the method
   * @param params the parameters to match the method
   * @return a wrapper of the method
   */
  @NonNull
  public static WrappedMethod of(
      @NonNull String className, @NonNull String name, @NonNull Class<?> params) {
    return WrappedClass.forName(className).getMethod(params, name);
  }

  /**
   * Invoke the method
   *
   * @param object the instance of the object to invoke the method if the method is static it may be
   *     null
   * @param params the parameters to invoke the method
   * @return if the method returns a type it will return the object if the method could not be
   *     invoked or it is a void method {@code null}
   */
  public Object invoke(Object object, Object... params) {
    if (this.reference == null) return null;
    try {
      return this.reference.invoke(object, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * The type that the method returns when {@link #invoke(Object, Object...)}
   *
   * @return an {@link Optional} instance holding the return type
   */
  @NonNull
  public Optional<Class<?>> getReturnType() {
    return Optional.ofNullable(this.reference == null ? null : this.reference.getReturnType());
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
    WrappedMethod that = (WrappedMethod) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }
}
