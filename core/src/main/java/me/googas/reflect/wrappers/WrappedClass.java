package me.googas.reflect.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.reflect.utility.ReflectUtil;

/**
 * This class wraps a {@link Class} to use its methods checking if those can be executed and
 * returning objects without throwing errors.
 *
 * <p>The method {@link Class#forName(String)} would throw a {@link ClassNotFoundException} mean
 * while {@link WrappedClass#forName(String)} will just return an empty instance if that is the case
 * most of the methods declared in this class would return empty instances too
 */
public class WrappedClass extends LangWrapper<Class<?>> {

  private WrappedClass(Class<?> clazz) {
    super(clazz);
  }

  private WrappedClass() {
    this(null);
  }

  /**
   * Return the wrapper of the {@link Class} object if {@link Class#forName(String)} matches a class
   * else it would be empty
   *
   * @see Class#forName(String)
   * @param name the fully qualified name of the class
   * @return the wrapped {@link Class} instance
   */
  @NonNull
  public static WrappedClass forName(@NonNull String name) {
    try {
      return new WrappedClass(Class.forName(name));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return new WrappedClass();
  }

  /**
   * Wrap a {@link Class} instance
   *
   * @param clazz the class to wrap
   * @return the wrapper of {@link Class}
   */
  @NonNull
  public static WrappedClass of(Class<?> clazz) {
    return new WrappedClass(clazz);
  }

  /**
   * Get the constructor matching the given parameters
   *
   * @param params the parameters to match the constructor with
   * @return a {@link WrappedConstructor} instance containing the constructor or empty if not found
   */
  @NonNull
  public WrappedConstructor getConstructor(Class<?>... params) {
    if (this.reference != null && this.hasConstructor(params)) {
      try {
        return WrappedConstructor.of(this.reference.getConstructor(params));
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    return new WrappedConstructor();
  }

  /**
   * Get the field matching the name
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField getField(@NonNull String name) {
    if (this.reference != null && this.hasField(name)) {
      try {
        return WrappedField.of(this.reference.getField(name));
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    return new WrappedField();
  }

  /**
   * Get a declared field matching the name
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField getDeclaredField(@NonNull String name) {
    if (this.reference != null && this.hasDeclaredField(name)) {
      try {
        return WrappedField.of(this.reference.getDeclaredField(name));
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    return new WrappedField();
  }

  /**
   * Get a method matching the name and parameter types
   *
   * @param name the name to match the method with
   * @param params the parameters to match the method with
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public WrappedMethod getMethod(@NonNull String name, Class<?>... params) {
    if (this.reference != null && this.hasMethod(name, params)) {
      try {
        return WrappedMethod.of(this.reference.getMethod(name, params));
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    return new WrappedMethod();
  }

  /**
   * Get a method matching the name, parameter types and return type
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedReturnMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedReturnMethod<T> getMethod(
      Class<T> returnType, @NonNull String name, Class<?>... params) {
    WrappedMethod method = this.getMethod(name, params);
    if (method.getMethod() != null && returnType != null) {
      Optional<Class<?>> optionalReturnType = method.getReturnType();
      if (optionalReturnType.isPresent() && returnType.isAssignableFrom(optionalReturnType.get()))
        return WrappedReturnMethod.of(method.getMethod(), returnType);
    }
    return WrappedReturnMethod.of(null, returnType);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class
   *
   * @param name the name of the method to find
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasMethod(@NonNull String name, Class<?>... params) {
    if (this.reference == null) return false;
    for (Method method : this.reference.getMethods()) {
      if (method.getName().equals(name)) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (ReflectUtil.compareParameters(paramTypes, params)) return true;
      }
    }
    return false;
  }

  /**
   * Checks if a field with the given name exists in the class
   *
   * @param name the name of the field to find
   * @return true if the field is found false otherwise
   */
  public boolean hasField(@NonNull String name) {
    if (this.reference != null) {
      for (Field field : this.reference.getFields()) {
        if (field.getName().equals(name)) return true;
      }
    }
    return false;
  }

  /**
   * Checks if a declared field with the given name exists in the class
   *
   * @param name the name of the field to find
   * @return true if the field is found false otherwise
   */
  public boolean hasDeclaredField(@NonNull String name) {
    if (this.reference != null) {
      for (Field field : this.reference.getDeclaredFields()) {
        if (field.getName().equals(name)) return true;
      }
    }
    return false;
  }

  /**
   * Checks if a constructor with the given parameter types exists in the class
   *
   * @param params the parameters of the constructor to find
   * @return true if the constructor is found false otherwise
   */
  public boolean hasConstructor(Class<?>... params) {
    if (this.reference == null) return false;
    for (Constructor<?> constructor : this.reference.getConstructors()) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      return ReflectUtil.compareParameters(paramTypes, params);
    }
    return false;
  }

  /**
   * Get a list of {@link Method} of the class
   *
   * @see {@link Class#getMethods()}
   * @return the list of methods
   */
  @NonNull
  public List<Method> getMethods() {
    if (this.reference != null) {
      return new ArrayList<>(Arrays.asList(this.reference.getMethods()));
    }
    return new ArrayList<>();
  }

  /**
   * Get a list of {@link Field} of the class
   *
   * @see {@link Class#getFields()}
   * @return the list of fields
   */
  @NonNull
  public List<Field> getFields() {
    if (this.reference != null) {
      return new ArrayList<>(Arrays.asList(this.reference.getFields()));
    }
    return new ArrayList<>();
  }

  /**
   * Get a list of {@link Field} that are declared in the class
   *
   * @see {@link Class#getDeclaredFields()}
   * @return the list of fields
   */
  @NonNull
  public List<Field> getDeclaredFields() {
    if (this.reference != null) {
      return new ArrayList<>(Arrays.asList(this.reference.getDeclaredFields()));
    }
    return new ArrayList<>();
  }

  /**
   * Get the wrapped class
   *
   * @return the wrapped class
   */
  @NonNull
  public Class<?> getClazz() {
    return this.reference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedClass that = (WrappedClass) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedClass.class.getSimpleName() + "[", "]")
        .add("clazz=" + reference)
        .toString();
  }
}
