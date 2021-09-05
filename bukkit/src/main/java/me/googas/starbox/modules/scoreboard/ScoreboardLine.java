package me.googas.starbox.modules.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import org.bukkit.OfflinePlayer;

/** Represents a line in a {@link PlayerBoard}. */
public class ScoreboardLine {

  @NonNull @Getter private final BukkitLine child;
  @Getter private final int position;

  /**
   * Create the line.
   *
   * @param child the line that this will build
   * @param position the position in which the line will be
   */
  public ScoreboardLine(@NonNull BukkitLine child, int position) {
    this.child = child;
    this.position = position;
  }

  /**
   * Parse the line.
   *
   * @see BukkitLine#parse(String)
   * @param string the string to parse the line
   * @param position the position in which the line will be
   * @return the parsed line
   */
  @NonNull
  public static ScoreboardLine parse(@NonNull String string, int position) {
    return new ScoreboardLine(BukkitLine.parse(string).formatSample(), position);
  }

  /**
   * Parse a layout of lines.
   *
   * @param layout the layout to parse
   * @param reverse whether to reverse the string list
   * @return the parsed lines in a {@link List}
   */
  @NonNull
  public static List<ScoreboardLine> parse(@NonNull List<String> layout, boolean reverse) {
    List<ScoreboardLine> lines = new ArrayList<>();
    if (reverse) Collections.reverse(layout);
    for (int position = 0; position < layout.size(); position++) {
      lines.add(ScoreboardLine.parse(layout.get(position), position));
    }
    return lines;
  }

  /**
   * Build this line.
   *
   * @param player the player to build this line to
   * @return the built line
   */
  @NonNull
  public String build(@NonNull OfflinePlayer player) {
    return this.child.asTextWithPlaceholders(player).orElse("");
  }
}
