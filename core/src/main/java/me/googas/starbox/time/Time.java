package me.googas.starbox.time;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.annotations.TimeAmount;
import me.googas.starbox.time.formatter.TimeFormatter;
import me.googas.starbox.time.unit.StarboxUnit;
import me.googas.starbox.time.unit.Unit;

/**
 * This class represents an amount of time such as: '1.3 years'
 *
 * <p>Similar to {@link java.time.Duration}.
 *
 * <p>This class is immutable. It is not too precise as the {@link #value} is a double which
 * represents the value of the {@link #unit} and all the units values are given in {@link
 * StarboxUnit#getMillis()}.
 *
 * <p>It does not allow negative values and trying to initialize it with negatives will throw a
 * {@link IllegalArgumentException}
 */
public class Time implements TemporalAmount {

  /** A constant for a duration of 0 milliseconds */
  @NonNull public static final Time ZERO = new Time(0, Unit.MILLIS);

  @Getter private final double value;
  @NonNull @Getter private final StarboxUnit unit;

  /**
   * Create a new time instance.
   *
   * <p>If you want to create 2 Seconds will be:
   *
   * <pre>
   * Time seconds = Time.of(2, Unit.SECONDS);
   * </pre>
   *
   * @param value the amount of time
   * @param unit the unit of the amount of time
   */
  private Time(double value, @NonNull StarboxUnit unit) {
    if (value < 0) throw new IllegalArgumentException("value must be higher than 0");
    this.value = value;
    this.unit = unit;
  }

  /**
   * Get an instance of time from an amount of millis, if effective is set as true it will match the
   * best {@link StarboxUnit} like: 1000 millis the effective unit with a value of 1 and 907200000
   * millis will be weeks and 1.5.
   *
   * <p>If effective is false the unit will be units and the value will be the given
   *
   * <pre>
   * Time halfWeek = Time.ofMillis(907200000, true);
   * </pre>
   *
   * @param millis the millis to get the time from
   * @param effective whether to use an effective {@link StarboxUnit} and value
   * @return a new time instance
   * @throws IllegalArgumentException if millis is less than 0
   */
  @NonNull
  public static Time ofMillis(long millis, boolean effective) {
    if (millis < 0) throw new IllegalArgumentException("millis must be higher than 0");
    if (effective) {
      Unit unit = Unit.fromMillis(millis);
      return new Time(unit.getDuration(millis), unit);
    }
    return new Time(millis, Unit.MILLIS);
  }

  /**
   * Get a new instance with the given amount: value and unit.
   *
   * <p>The next example gives an instance of 2 seconds.
   *
   * <pre>
   *  Time seconds = Time.of(2, Unit.SECONDS);
   *  </pre>
   *
   * @param value the value of the amount of time
   * @param unit the unit of the amount of time
   * @return a new time instance
   */
  @NonNull
  public static Time of(double value, @NonNull StarboxUnit unit) {
    return new Time(value, unit);
  }

  /**
   * Parse an instance using the annotation {@link TimeAmount}. If the annotation {@link
   * TimeAmount#string()} is not empty it will be parsed using {@link #parse(String, boolean)} using
   * effective as true, if it is not empty then it will be parsed using {@link #of(double,
   * StarboxUnit)}
   *
   * @param timeAmount the annotation to get the instance of time of
   * @return a new time instance
   */
  @NonNull
  public static Time of(@NonNull TimeAmount timeAmount) {
    if (timeAmount.string().isEmpty()) {
      return Time.of(timeAmount.value(), timeAmount.unit());
    } else {
      return Time.parse(timeAmount.string(), true);
    }
  }

  /**
   * Parses an amount of time from the string. The string from {@link Time#toString()} can be used
   * in this method. The effective boolean acts the same as {@link #ofMillis(long, boolean)}.
   *
   * <p>This works as follows: [value][{@link Unit#getSingle()}] the unit single ignores casing.
   *
   * <p>So:
   *
   * <ul>
   *   <li>'2s' = 2 SECONDS
   *   <li>'1y' = 1 YEARS
   *   <li>'2.5d' = 2.5 DAYS
   * </ul>
   *
   * You can also chain values:
   *
   * <ul>
   *   <li>'2s2s2s2s2s' = 10 SECONDS
   *   <li>'1o2w' = 1.5 MONTHS
   *   <li>'2y6o2w' = 2.538 YEARS
   * </ul>
   *
   * @see #ofMillis(long, boolean)
   * @param string the string to forName as time
   * @param effective whether to use an effective {@link StarboxUnit} and value
   * @return the parsed instance of time
   * @throws IllegalArgumentException if no unit is matched
   */
  @NonNull
  public static Time parse(@NonNull String string, boolean effective) {
    if (string.isEmpty()) throw new IllegalArgumentException("Received an empty string");
    long millis = 0;
    int state = 0;
    int last = 0;
    double value = 0;
    for (int i = 0; i < string.length(); i++) {
      switch (string.charAt(i)) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
        case '.':
          if (state == 2) {
            millis += Unit.fromString(string.substring(last, i)).getMillis(value);
            last = i;
          }
          state = 1;
          break;
        default:
          if (state == 1) {
            value = Double.parseDouble(string.substring(last, i));
            last = i;
          }
          state = 2;
          break;
      }
    }
    millis += Unit.fromString(string.substring(last)).getMillis(value);
    return Time.ofMillis(millis, effective);
  }

  /**
   * Duplicate this same instance
   *
   * @return the duplicated instance of this time
   */
  @NonNull
  private Time abs() {
    return new Time(this.value, this.unit);
  }

  /**
   * Get the value of this amount of time in millis so: 10 seconds = 10000 millis
   *
   * @return the amount of time in millis
   */
  public double toMillis() {
    return this.value * this.unit.getMillis();
  }

  /**
   * Same as {@link #toMillis()} but the result will be rounded using {@link Math#round(double)}
   *
   * @see Math#round(double)
   * @return the result from {@link #toMillis()} rounded
   */
  public long toMillisRound() {
    return Math.round(this.toMillis());
  }

  /**
   * Get the amount of time in the given unit
   *
   * <p>This divides {@link #toMillis()} with {@link StarboxUnit#getMillis()}
   *
   * <p>So
   *
   * <pre>
   * Time day = Time.of(1, Unit.DAYS);
   * System.out.println("Output 24: " + day.get(Unit.HOURS));
   * </pre>
   *
   * @param unit the unit to get the amount of time
   * @return the amount of time in the given unit
   */
  public double get(@NonNull StarboxUnit unit) {
    return this.toMillis() / unit.getMillis();
  }

  /**
   * Format this time in the given formatter
   *
   * @see me.googas.starbox.time.formatter.HhMmSsFormatter
   * @see TimeFormatter#format(Time)
   * @param formatter the time formatter to format this time
   * @return the formatted {@link String}
   */
  @NonNull
  public String format(@NonNull TimeFormatter formatter) {
    return formatter.format(this);
  }

  /**
   * Get this amount of time in a different {@link StarboxUnit}
   *
   * @param unit the unit to convert the time to
   * @return a new time instance with the new unit
   */
  @NonNull
  public Time getAs(@NonNull StarboxUnit unit) {
    return new Time(this.toMillis() / unit.getMillis(), unit);
  }

  public long getValueRound() {
    return Math.round(this.value);
  }

  @Override
  public long get(TemporalUnit temporalUnit) {
    if (temporalUnit == this.unit) {
      return (long) this.value;
    }
    return 0;
  }

  @Override
  public List<TemporalUnit> getUnits() {
    return Collections.singletonList(this.unit);
  }

  @Override
  public Temporal addTo(Temporal temporal) {
    if (this.value > 0) {
      temporal = temporal.plus(Math.round(this.value), this.unit);
    }
    return temporal;
  }

  @Override
  public Temporal subtractFrom(Temporal temporal) {
    if (this.value > 0) {
      temporal = temporal.plus(Math.round(this.value), this.unit);
    }
    return temporal;
  }

  @Override
  public String toString() {
    StarboxUnit current = this.unit;
    long millis = this.toMillisRound();
    StringBuilder builder = new StringBuilder();
    List<Unit> list = Arrays.asList(Unit.values());
    Collections.reverse(list);
    for (Unit unit : list) {
      if (unit.getMillis() <= current.getMillis()) {
        long value = (long) unit.getDuration(millis);
        if (value <= 0) continue;
        millis -= unit.getMillis(value);
        builder.append(value).append(Character.toLowerCase(unit.getSingle()));
      }
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Time time = (Time) o;
    return Double.compare(time.value, value) == 0 && Objects.equals(unit, time.unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, unit);
  }
}
