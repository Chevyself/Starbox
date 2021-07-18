package me.googas.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

public class WrappedMethod {

  @Getter private final Method method;

  public WrappedMethod(Method method) {
    this.method = method;
    if (method != null) method.setAccessible(true);
  }

  public WrappedMethod() {
    this(null);
  }

  @NonNull
  public static WrappedMethod parse(
      @NonNull Class<?> clazz, @NonNull String name, @NonNull Class<?>... params) {
    return new WrappedClass(clazz).getMethod(name, params);
  }

  @NonNull
  public static WrappedMethod parse(
      @NonNull String className, @NonNull String name, @NonNull Class<?> params) {
    return WrappedClass.parse(className).getMethod(params, name);
  }

  public Object invoke(Object object, Object... params) {
    if (this.method == null) return null;
    try {
      return this.method.invoke(object, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Class<?> getReturnType() {
    if (this.method == null) return null;
    return this.method.getReturnType();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedMethod.class.getSimpleName() + "[", "]")
        .add("method=" + method)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedMethod that = (WrappedMethod) o;
    return Objects.equals(method, that.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method);
  }
}
