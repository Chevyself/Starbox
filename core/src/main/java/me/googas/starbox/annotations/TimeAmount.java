package me.googas.starbox.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.NonNull;
import me.googas.starbox.time.unit.StarboxUnit;
import me.googas.starbox.time.unit.Unit;

/**
 * This represents {@link me.googas.starbox.time.Time} as an annotation
 *
 * <p>See {@link me.googas.starbox.time.Time#of(TimeAmount)} to know how it is parsed
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAmount {

  /**
   * Get the value of the amount of time. This is used {@link me.googas.starbox.time.Time#of(double,
   * StarboxUnit)}
   *
   * @return the value of the amount of time
   */
  double value() default 0;

  /**
   * Get the unit of the amount of time. This is used {@link me.googas.starbox.time.Time#of(double,
   * StarboxUnit)}
   *
   * @return the unit of the amount of time
   */
  Unit unit() default Unit.MILLIS;

  /**
   * Get the {@link String} to forName the amount of time instead of using {@link #value()} and
   * {@link #unit()}.
   *
   * @return the {@link String} that can be parsed using {@link
   *     me.googas.starbox.time.Time#parse(String, boolean)}
   */
  @NonNull
  String string() default "";
}
