package me.googas.starbox.modules.language;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import me.googas.starbox.builders.Line;

/** Formatter for sample lines. */
public class SampleFormatter implements Line.Formatter, BukkitLine.LocalizedFormatter {

  @NonNull private static final Pattern PATTERN = Pattern.compile("(?<!\\w)\\$\\w([^\\s]+)");

  SampleFormatter() {}

  @Override
  public @NonNull Line format(@NonNull Line line) {
    if (line instanceof BukkitLine) {
      Locale locale;
      if (line instanceof BukkitLine.Localized) {
        locale = ((BukkitLine.Localized) line).getLocale();
      } else {
        locale = Locale.ENGLISH;
      }
      return this.format(locale, (BukkitLine) line);
    }
    return line;
  }

  @Override
  public @NonNull BukkitLine format(@NonNull Locale locale, @NonNull BukkitLine line) {
    String raw = line.getRaw();
    Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
    while (matcher.find()) {
      String key = matcher.group().replace("\"", "");
      BukkitLine bukkitLine = BukkitLine.parse(key);
      String replacement;
      if (bukkitLine instanceof BukkitLine.LocalizedReference) {
        BukkitLine.LocalizedReference reference = (BukkitLine.LocalizedReference) bukkitLine;
        replacement = reference.asLocalized(locale).getRaw();
      } else {
        replacement = bukkitLine.getRaw();
      }
      raw = raw.replace(key, replacement);
    }
    return (BukkitLine) line.setRaw(raw);
  }
}
