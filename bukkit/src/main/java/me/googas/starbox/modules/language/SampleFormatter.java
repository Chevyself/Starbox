package me.googas.starbox.modules.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import me.googas.starbox.builders.Line;

/** Formatter for sample lines. */
public class SampleFormatter implements Line.Formatter {

  @NonNull private static final Pattern PATTERN = Pattern.compile("(?<!\\w)\\$\\w([^\\s]+)");

  SampleFormatter() {}

  @Override
  public @NonNull Line format(@NonNull Line line) {
    if (line instanceof BukkitLine.Localized) {
      String raw = ((BukkitLine) line).getRaw();
      Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
      while (matcher.find()) {
        String key = matcher.group().substring(1).replace("\"", "");
        BukkitLine bukkitLine =
            BukkitLine.localized(((BukkitLine.Localized) line).getLocale(), key);
        raw = raw.replace("$" + key, bukkitLine.getRaw());
      }
      return ((BukkitLine) line).setRaw(raw);
    }
    return line;
  }
}
