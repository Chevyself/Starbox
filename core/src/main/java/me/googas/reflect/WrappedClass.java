package me.googas.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

public class WrappedClass {

  @Getter private final Class<?> clazz;

  public WrappedClass(Class<?> clazz) {
    this.clazz = clazz;
  }

  public WrappedClass() {
    this(null);
  }

  public static boolean compareParameters(Class<?>[] paramTypes, Class<?>[] params) {
    if (paramTypes == null || params == null) return false;
    if (paramTypes.length != params.length) return false;
    for (int i = 0; i < paramTypes.length; i++) {
      if (paramTypes[i] != params[i]) return false;
    }
    return true;
  }

  @NonNull
  public static WrappedClass parse(@NonNull String name) {
    try {
      return new WrappedClass(Class.forName(name));
    } catch (ClassNotFoundException ignored) {
    }
    return new WrappedClass();
  }

  @NonNull
  public WrappedConstructor getConstructor(Class<?>... params) {
    if (this.clazz != null && this.hasConstructor(params)) {
      try {
        return new WrappedConstructor(this.clazz.getConstructor(params));
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    return new WrappedConstructor();
  }

  @NonNull
  public WrappedField getField(@NonNull String name) {
    if (this.clazz != null && this.hasField(name)) {
      try {
        return new WrappedField(this.clazz.getField(name));
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    return new WrappedField();
  }

  @NonNull
  public WrappedField getDeclaredField(@NonNull String name) {
    if (this.clazz != null && this.hasDeclaredField(name)) {
      try {
        return new WrappedField(this.clazz.getDeclaredField(name));
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    return new WrappedField();
  }

  @NonNull
  public WrappedMethod getMethod(@NonNull String name, Class<?>... params) {
    if (this.clazz != null && this.hasMethod(name, params)) {
      try {
        return new WrappedMethod(this.clazz.getMethod(name, params));
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    return new WrappedMethod();
  }

  @NonNull
  public <T> WrappedReturnMethod<T> getMethod(
      Class<T> returnType, @NonNull String name, Class<?>... params) {
    WrappedMethod method = this.getMethod(name, params);
    if (method.getMethod() != null && returnType != null) {
      Class<?> methodReturnType = method.getReturnType();
      if (methodReturnType != null && returnType.isAssignableFrom(methodReturnType))
        return new WrappedReturnMethod<>(method.getMethod(), returnType);
    }
    return new WrappedReturnMethod<>(null, returnType);
  }

  public boolean hasMethod(@NonNull String name, Class<?>... params) {
    if (this.clazz == null) return false;
    for (Method method : this.clazz.getMethods()) {
      if (method.getName().equals(name)) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (WrappedClass.compareParameters(paramTypes, params)) return true;
      }
    }
    return false;
  }

  public boolean hasField(@NonNull String name) {
    if (this.clazz != null) {
      for (Field field : this.clazz.getFields()) {
        if (field.getName().equals(name)) return true;
      }
    }
    return false;
  }

  public boolean hasDeclaredField(@NonNull String name) {
    if (this.clazz != null) {
      for (Field field : this.clazz.getDeclaredFields()) {
        if (field.getName().equals(name)) return true;
      }
    }
    return false;
  }

  public boolean hasConstructor(Class<?>... params) {
    if (this.clazz == null) return false;
    for (Constructor<?> constructor : this.clazz.getConstructors()) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      return WrappedClass.compareParameters(paramTypes, params);
    }
    return false;
  }

  @NonNull
  public List<Method> getMethods() {
    if (this.clazz != null) {
      return new ArrayList<>(Arrays.asList(this.clazz.getMethods()));
    }
    return new ArrayList<>();
  }

  @NonNull
  public List<Field> getFields() {
    if (this.clazz != null) {
      return new ArrayList<>(Arrays.asList(this.clazz.getFields()));
    }
    return new ArrayList<>();
  }

  @NonNull
  public List<Field> getDeclaredFields() {
    if (this.clazz != null) {
      return new ArrayList<>(Arrays.asList(this.clazz.getDeclaredFields()));
    }
    return new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedClass that = (WrappedClass) o;
    return Objects.equals(clazz, that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clazz);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedClass.class.getSimpleName() + "[", "]")
        .add("clazz=" + clazz)
        .toString();
  }
}
