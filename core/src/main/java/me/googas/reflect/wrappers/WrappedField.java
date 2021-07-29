package me.googas.reflect.wrappers;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;

/** This class wraps a {@link Field} to set or get the declaration */
public class WrappedField extends LangWrapper<Field> {

  private WrappedField(Field reference) {
    super(reference);
  }

  WrappedField() {
    this(null);
  }

  /**
   * Wrap a {@link Field} instance
   *
   * @param field the field to wrap
   * @return the wrapper of the field
   */
  @NonNull
  public static WrappedField of(Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField(field);
  }

  /**
   * Get the value that is stored in the field for the parameter object
   *
   * @param obj the object to get the value of the field from
   * @return the value of the field for the parameter object or null if the value could not be
   *     obtained
   */
  public Object get(@NonNull Object obj) {
    if (this.reference != null) {
      try {
        return this.reference.get(obj);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Set the value of the field in an object
   *
   * @param object the object to set the value of the field to
   * @param value the new value to set on the field
   * @return true if the value was set false otherwise
   */
  public boolean set(@NonNull Object object, Object value) {
    if (this.reference != null) {
      try {
        this.reference.set(object, value);
        return true;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  /**
   * Get the instance of wrapped {@link Field}
   *
   * @return the wrapped field if present else null
   */
  public Field getField() {
    return reference;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedField.class.getSimpleName() + "[", "]")
        .add("field=" + reference)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedField that = (WrappedField) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }
}
