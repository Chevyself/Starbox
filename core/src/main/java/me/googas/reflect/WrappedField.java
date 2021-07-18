package me.googas.reflect;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;

public class WrappedField {

  @Getter private final Field field;

  public WrappedField(Field field) {
    this.field = field;
    if (field != null) field.setAccessible(true);
  }

  public WrappedField() {
    this(null);
  }

  public Object get(@NonNull Object obj) {
    if (this.field != null) {
      try {
        return this.field.get(obj);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public boolean set(@NonNull Object object, Object value) {
    if (this.field != null) {
      try {
        this.field.set(object, value);
        return true;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedField.class.getSimpleName() + "[", "]")
        .add("field=" + field)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedField that = (WrappedField) o;
    return Objects.equals(field, that.field);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field);
  }
}
