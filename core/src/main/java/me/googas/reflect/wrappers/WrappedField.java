package me.googas.reflect.wrappers;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.starbox.expressions.HandledExpression;

/** This class wraps a {@link Field} to set or get the declaration. */
public class WrappedField<O> extends LangWrapper<Field> {

  private final Class<O> fieldType;

  private WrappedField(Field reference, Class<O> fieldType) {
    super(reference);
    this.fieldType = fieldType;
  }

  WrappedField() {
    this(null, null);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param field the field to wrap
   * @return the wrapper of the field
   */
  @NonNull
  public static WrappedField<?> of(Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField<>(field, null);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param fieldType the class of the object that the field contains
   * @param field the field to wrap
   * @param <T> the type of the object in the field
   * @return the wrapped field
   */
  @NonNull
  public static <T> WrappedField<T> of(Class<T> fieldType, Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField<>(field, fieldType);
  }

  /**
   * Get the value that is stored in the field for the parameter object.
   *
   * @param obj the object to get the value of the field from
   * @return a {@link HandledExpression} which gets the object in the field or handles a {@link
   *     IllegalAccessException}
   */
  @NonNull
  public HandledExpression<Object> provide(@NonNull Object obj) {
    return HandledExpression.using(
        () -> {
          Object other = null;
          if (this.reference != null) {
            other = this.reference.get(obj);
          }
          return other;
        });
  }

  /**
   * Get the value that is stored in the field for the parameter object and cast it to the field
   * type.
   *
   * @param instance the object to get the value of the field from
   * @return a {@link HandledExpression} which gets the object in the field or handles a {@link
   *     IllegalAccessException}
   */
  public HandledExpression<O> get(Object instance) {
    return HandledExpression.using(
        () -> {
          O other = null;
          if (this.reference != null && this.fieldType != null) {
            other = this.fieldType.cast(this.reference.get(instance));
          }
          return other;
        });
  }

  /**
   * Set the value of the field in an object.
   *
   * @param object the object to set the value of the field to
   * @param value the new value to set on the field
   * @return a {@link HandledExpression} which returns whether the new value was set or handles
   *     {@link IllegalAccessException}
   */
  @NonNull
  public HandledExpression<Boolean> set(@NonNull Object object, Object value) {
    return HandledExpression.using(
        () -> {
          boolean set = false;
          if (this.reference != null) {
            this.reference.set(object, value);
            set = true;
          }
          return set;
        });
  }

  /**
   * Get the instance of wrapped {@link Field}.
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
    WrappedField<?> that = (WrappedField<?>) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }
}
