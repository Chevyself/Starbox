package me.googas.starbox.modules.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import me.googas.starbox.builders.Line;

/** Formatter for sample lines. */
public class SampleFormatter implements Line.Formatter {

  @NonNull private static final Pattern PATTERN = Pattern.compile("%.*?%");

  SampleFormatter() {}

  @Override
  public @NonNull Line format(@NonNull Line line) {
    if (line instanceof BukkitLine) {
      String raw = ((BukkitLine) line).getRaw();
      Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
      while (matcher.find()) {
        String key = matcher.group().replace("%", "");
        BukkitLine bukkitLine =
            BukkitLine.parse(
                line instanceof BukkitLine.Localized
                    ? ((BukkitLine.Localized) line).getLocale()
                    : null,
                key);
        raw = raw.replace("%" + key + "%", bukkitLine.getRaw());
      }
      return ((BukkitLine) line).setRaw(raw);
    }
    throw new IllegalStateException(line + " is not a BukkitLine");
  }
}
