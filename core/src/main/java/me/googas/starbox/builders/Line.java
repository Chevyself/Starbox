package me.googas.starbox.builders;

import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

/**
 * This object represents a message to be build as an object or text. This includes methods to
 * format the message and even a {@link Formatter} to do so.
 */
public interface Line {

  /**
   * Build the message.
   *
   * @return the built message
   */
  @NonNull
  Object build();

  /**
   * Build the message as text.
   *
   * @return the built message as text
   */
  @NonNull
  Optional<String> asText();

  /**
   * Format the message using an array of objects.
   *
   * @param objects the objects to format the message
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Object... objects);

  /**
   * Format the message using a map of placeholders.
   *
   * @param map the map of the placeholders
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Map<String, String> map);

  /**
   * Format the message using a formatter.
   *
   * @param formatter the formatter to format the message
   * @return this same instance
   */
  @NonNull
  Line format(@NonNull Formatter formatter);

  /** This represents a formatter for lines. */
  interface Formatter {

    /**
     * Format a line.
     *
     * @param line the line to be formatted
     * @return the formatted line
     */
    @NonNull
    Line format(@NonNull Line line);
  }
}
