package me.googas.starbox.time.unit;

import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.time.Time;

/**
 * An enumeration implementing {@link StarboxUnit} as an standard for {@link Time}
 *
 * <p>All this units provide a way to manipulate time from milliseconds to years.
 *
 * <p>There's concepts like {@link java.time.temporal.ChronoUnit#FOREVER} or {@link
 * java.time.temporal.ChronoUnit#ERAS}
 */
public enum Unit implements StarboxUnit {
  /** Unit that represents a millisecond */
  MILLIS('L', 1, Duration.ofMillis(1)),
  /** Unit that represents a second */
  SECONDS('S', 1000, Duration.ofSeconds(1)),
  /** Unit that represents a minute */
  MINUTES('M', 60000, Duration.ofMinutes(1)),
  /** Unit that represents an hour */
  HOURS('H', 3600000, Duration.ofHours(1)),
  /** Unit that represents a day */
  DAYS('D', 86400000, Duration.ofDays(1)),
  /** Unit that represents a week */
  WEEKS('W', 604800000, Duration.ofMillis(604800000)),
  /** Unit that represents a month */
  MONTH('O', 2629800000L, Duration.ofMillis(2629800000L)),
  /** Unit that represents a year */
  YEARS('Y', 31557600000L, Duration.ofMillis(31557600000L));

  @Getter private final char single;
  @Getter private final long millis;
  @NonNull @Getter private final Duration duration;

  Unit(char single, long millis, @NonNull Duration duration) {
    this.single = single;
    this.millis = millis;
    this.duration = duration;
  }

  /**
   * Get the unit that matches the character. This means that:
   *
   * <ul>
   *   <li>'M' = {@link #MINUTES}
   *   <li>'S' = {@link #SECONDS}
   *   <li>'O' = {@link #YEARS}
   * </ul>
   *
   * @param single the character to match the unit to
   * @return the unit that matches the character
   * @throws IllegalArgumentException if the character does not match any {@link Unit#getSingle()}
   */
  @NonNull
  public static Unit fromCharacter(char single) {
    single = Character.toUpperCase(single);
    for (Unit value : Unit.values()) {
      if (value.getSingle() == single) return value;
    }
    throw new IllegalArgumentException(single + " does not match any Unit#getSingle()");
  }

  /**
   * Get the unit that matches the string. This will also check if the first character of the string
   * matches any {@link #getSingle()}, this means that:
   *
   * <ul>
   *   <li>'M' || "minutes" = {@link #MINUTES}
   *   <li>'S' || "seconds" = {@link #SECONDS}
   *   <li>'O' || "months" = {@link #MONTH}
   * </ul>
   *
   * @param string the string to match the unit to
   * @return the unit that matches the string
   * @throws IllegalArgumentException if the string is empty or if the string does not match {@link
   *     #getSingle()} or {@link #name()}
   */
  @NonNull
  public static Unit fromString(@NonNull String string) {
    if (string.isEmpty()) throw new IllegalArgumentException("Received an empty string");
    string = string.toUpperCase();
    for (Unit value : Unit.values()) {
      if (value.getSingle() == string.charAt(0) || value.name().equalsIgnoreCase(string))
        return value;
    }
    throw new IllegalArgumentException(
        string + " does not match any Unit#getSingle() or Unit#name()");
  }

  /**
   * Get the unit that matches the given millis. This will attempt to get the unit with the closer
   * {@link #getMillis()}
   *
   * <p>This means that:
   *
   * <ul>
   *   <li>5000 = {@link #SECONDS}
   *   <li>31567800000 = {@link #YEARS}
   *   <li>80000 = {@link #MINUTES}
   * </ul>
   *
   * @param millis the millis to match the unit to
   * @return the unit that matches the millis
   * @throws IllegalArgumentException if millis is lower than 0
   */
  @NonNull
  public static Unit fromMillis(long millis) {
    if (millis < 0) throw new IllegalArgumentException("millis must be higher than 0");
    Unit unit = Unit.MILLIS;
    for (Unit value : Unit.values()) {
      if (value.millis <= millis) {
        unit = value;
      }
    }
    return unit;
  }

  @Override
  public boolean isDurationEstimated() {
    return this.compareTo(Unit.DAYS) >= 0;
  }

  @Override
  public boolean isDateBased() {
    return this.compareTo(Unit.DAYS) >= 0;
  }

  @Override
  public boolean isTimeBased() {
    return this.compareTo(Unit.DAYS) < 0;
  }
}
