package me.googas.starbox.time.unit;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import lombok.NonNull;
import me.googas.starbox.time.Time;

/**
 * An unit of time such as seconds, minutes, days or years
 *
 * <p>The direct implementation to use in {@link Time} is {@link me.googas.starbox.time.unit.Unit}
 * but classes implementing this interface should represent an unit that is used to measure time.
 */
public interface StarboxUnit extends TemporalUnit {

  /**
   * Get the duration of this unit in the given millis.
   *
   * <p>This means that if the millis is 1000 and the unit is seconds the duration will be 1
   *
   * <p>More examples:
   *
   * <ul>
   *   <li>172,800,000 + DAYS = 2 Days
   *   <li>1,814,400,000 + WEEKS = 3 Weeks
   *   <li>1,800,000 + HOUR = 0.5 hours
   * </ul>
   *
   * @param millis the millis to get the duration of the unit
   * @return the duration of the unit given the millis
   */
  default double getDuration(double millis) {
    return millis / this.getMillis();
  }

  /**
   * Get a single character which may be used to represent the unit. Such as:
   *
   * <ul>
   *   <li>'M' minute
   *   <li>'S' second
   *   <li>'O' month
   *   <li>'Y' year
   * </ul>
   *
   * @return the single character that represents the unit
   */
  char getSingle();

  /**
   * Get this unit as millis. Such as:
   *
   * <ul>
   *   <li>second = 1000
   *   <li>minute = 60000
   *   <li>year = 31557600000L
   * </ul>
   *
   * @return the millis that represent the unit
   */
  long getMillis();

  /**
   * Get the duration in millis of the given value
   *
   * <p>For example:
   *
   * <pre>
   * double value = Unit.SECONDS.getMillis(2);
   * System.out.println("Output 2000: " + value);
   * </pre>
   *
   * This divides the parameter value with {@link #getMillis()}
   *
   * @param value the value to get the duration in millis from
   * @return the duration in millis
   */
  default double getMillis(double value) {
    return value * this.getMillis();
  }

  /**
   * Get an instance of time based on this unit.
   *
   * @return the instance of time based in this unit
   */
  @NonNull
  default Time getTime() {
    return Time.of(1, this);
  }

  @Override
  default <R extends Temporal> R addTo(R temporal, long amount) {
    temporal.plus(this.getDuration());
    return temporal;
  }

  @Override
  default long between(Temporal temporal, Temporal temporal1) {
    return temporal.until(temporal1, this);
  }
}
