package me.googas.reflect.reflect;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;

public class WrappedReturnMethod<T> extends WrappedMethod {

  private final Class<T> returnType;

  public WrappedReturnMethod(Method method, Class<T> returnType) {
    super(method);
    this.returnType = returnType;
  }

  public WrappedReturnMethod() {
    this(null, null);
  }

  @NonNull
  public T invokeOr(@NonNull Object object, @NonNull T def, @NonNull Object... params) {
    T t = this.invoke(object, params);
    return t == null ? def : t;
  }

  @Override
  public T invoke(Object object, @NonNull Object... params) {
    if (this.returnType == null) return null;
    Object t = super.invoke(object, params);
    if (t != null && this.returnType.isAssignableFrom(t.getClass())) return this.returnType.cast(t);
    return null;
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
