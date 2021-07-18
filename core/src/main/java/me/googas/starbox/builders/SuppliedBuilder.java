package me.googas.starbox.builders;

import java.util.function.Supplier;
import lombok.NonNull;

/**
 * Just like {@link Builder} but the type T represents the parameter to get the object.
 *
 * @param <T> the type of the parameter
 * @param <O> the type of the built object
 */
public interface SuppliedBuilder<T, O> {

  /**
   * Build the object
   *
   * @param t the parameter that takes the builder to build the object
   * @return the built object
   */
  O build(@NonNull T t);

  /**
   * Build the object or get a default instance in case that {@link #build(Object)} is null.
   *
   * @param t the parameter that takes the builder to build the object
   * @param def the default instance of the type to build in case that the object built is null
   * @return the object built if it is not null else the default parameter object
   */
  @NonNull
  default O buildOr(@NonNull T t, @NonNull O def) {
    O build = this.build(t);
    return build == null ? def : build;
  }

  /**
   * Build the object or get a default instance using a {@link Supplier} in case that {@link
   * #build(Object)} is null
   *
   * @param t the parameter that takes the builder to build the object
   * @param supplier the supplier of the default instance of the type to build in case that the
   *     object built is null
   * @return the object built if it is not null else the object given by the supplier parameter
   */
  @NonNull
  default O buildOrGet(@NonNull T t, @NonNull Supplier<O> supplier) {
    O build = this.build(t);
    return build == null ? supplier.get() : build;
  }
}
