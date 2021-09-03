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
    if (line instanceof BukkitLine) {
      String raw = ((BukkitLine) line).getRaw();
      Matcher matcher = SampleFormatter.PATTERN.matcher(raw);
      while (matcher.find()) {
        String key = matcher.group().replace("\"", "");
        BukkitLine bukkitLine = BukkitLine.parse(key);
        String replacement;
        if (line instanceof BukkitLine.Localized
            && bukkitLine instanceof BukkitLine.LocalizedReference) {
          BukkitLine.LocalizedReference reference = (BukkitLine.LocalizedReference) bukkitLine;
          replacement = reference.asLocalized().getRaw();
        } else {
          replacement = bukkitLine.getRaw();
        }
        raw = raw.replace(key, replacement);
      }
      return ((BukkitLine) line).setRaw(raw);
    }
    return line;
  }
}
