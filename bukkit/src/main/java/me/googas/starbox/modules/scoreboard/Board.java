package me.googas.starbox.modules.scoreboard;

import java.util.List;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import org.bukkit.entity.Player;

/**
 * Represents a {@link org.bukkit.scoreboard.Scoreboard}.
 */
public interface Board {

  /**
   * Create the scoreboard for a player.
   *
   * @param player the player to create the scoreboard to
   * @param layout the layout of the scoreboard
   * @return the scoreboard
   */
  static PlayerBoard create(@NonNull Player player, @NonNull List<ScoreboardLine> layout) {
    return PlayerBoard.create(player, layout);
  }

  /**
   * Set a line in the scoreboard.
   *
   * @param line the line to set
   * @return this same instance
   */
  @NonNull
  Board set(@NonNull ScoreboardLine line);

  /**
   * Add a line to this scoreboard.
   *
   * @param line the line to add
   * @return this same instance
   */
  @NonNull
  Board add(@NonNull BukkitLine line);

  /** Destroy this scoreboard. */
  void destroy();

  /**
   * Update the layout of this board.
   *
   * @return this same instance
   */
  @NonNull
  Board update();

  /**
   * Updates the line in the given position.
   *
   * @param index the position of the line to update
   * @return this same instance
   */
  @NonNull
  Board update(int index);

  /**
   * Set the title of the scoreboard.
   *
   * @param title the new title of the scoreboard
   * @return this same instance
   */
  @NonNull
  Board setTitle(String title);

  /**
   * Set the layout of this scoreboard.
   *
   * @param layout the new layout
   * @return this same instance
   */
  @NonNull
  Board setLayout(@NonNull List<ScoreboardLine> layout);
}
