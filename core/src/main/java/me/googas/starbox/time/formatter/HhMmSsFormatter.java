package me.googas.starbox.time.formatter;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.time.Time;

/**
 * Formats time into the 'Hh:Mm:Ss' format:
 *
 * <p>If you give this formatter 'Time.forName("1d12m59s")'
 *
 * <p>The formatted {@link String} will be: "24:12:59"
 */
public class HhMmSsFormatter implements TimeFormatter {

  @NonNull @Getter private static final HhMmSsFormatter formatter = new HhMmSsFormatter();

  private HhMmSsFormatter() {}

  @Override
  public @NonNull String format(@NonNull Time time) {
    long millis = Math.round(time.toMillis());
    long secs = (millis / 1000) % 60;
    long minutes = (millis / (1000 * 60)) % 60;
    long hours = millis / (1000 * 60 * 60);
    return String.format("%02d:%02d:%02d", hours, minutes, secs);
  }
}
