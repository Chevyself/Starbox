package me.googas.starbox.modules.scoreboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import me.googas.starbox.BukkitLine;
import org.bukkit.entity.Player;

/** A scoreboard that contains the scoreboard of multiple players. */
public class MultiBoard implements Board {

  @NonNull private final Set<PlayerBoard> scoreboards = new HashSet<>();
  @NonNull private List<ScoreboardLine> layout = new ArrayList<>();
  @NonNull private String title = "";

  @Override
  public @NonNull MultiBoard set(@NonNull ScoreboardLine line) {
    scoreboards.forEach(scoreboard -> scoreboard.set(line));
    int position = line.getPosition();
    int size = layout.size();
    for (int i = this.layout.size(); i < position + 1; i++) {
      this.layout.add(new ScoreboardLine(BukkitLine.of(" "), i));
    }
    this.layout.set(line.getPosition(), line);
    return this;
  }

  @Override
  public @NonNull MultiBoard add(@NonNull BukkitLine line) {
    scoreboards.forEach(scoreboard -> scoreboard.add(line));
    int position = this.layout.isEmpty() ? 0 : this.layout.size() - 1;
    return this.set(new ScoreboardLine(line, position));
  }

  @Override
  public void destroy() {
    this.scoreboards.forEach(PlayerBoard::destroy);
    this.scoreboards.clear();
  }

  @Override
  public @NonNull MultiBoard update() {
    scoreboards.forEach(PlayerBoard::update);
    return this;
  }

  @Override
  public @NonNull MultiBoard update(int index) {
    scoreboards.forEach(scoreboard -> scoreboard.update(index));
    return this;
  }

  @Override
  public @NonNull MultiBoard setTitle(String title) {
    scoreboards.forEach(scoreboard -> scoreboard.setTitle(title));
    this.title = title;
    return this;
  }

  @Override
  public @NonNull MultiBoard setLayout(@NonNull List<ScoreboardLine> layout) {
    scoreboards.forEach(scoreboard -> scoreboard.setLayout(layout));
    this.layout = layout;
    return this;
  }

  /**
   * Add a player to this board.
   *
   * @param player the player to add
   * @return this same instance
   */
  @NonNull
  public MultiBoard add(@NonNull Player player) {
    Optional<PlayerBoard> optional = this.getBoard(player);
    if (optional.isPresent()) {
      optional.get().apply();
    } else {
      this.scoreboards.add(Board.create(player, this.layout).initialize(this.title));
    }
    return this;
  }

  /**
   * Get the board of a player.
   *
   * @param player the player to get the board from
   * @return a {@link Optional} holding the nullable board
   */
  @NonNull
  public Optional<PlayerBoard> getBoard(@NonNull Player player) {
    return this.getBoard(player.getUniqueId());
  }

  /**
   * Get the board of a player using its {@link UUID}.
   *
   * @param uniqueId the unique id of the player to get the board from
   * @return a {@link Optional} holding the nullable board
   */
  @NonNull
  public Optional<PlayerBoard> getBoard(@NonNull UUID uniqueId) {
    return this.scoreboards.stream()
        .filter(board -> board.getUniqueId().equals(uniqueId))
        .findFirst();
  }

  /**
   * Remove a player from this board.
   *
   * @param player the player to remove
   * @return this same instance
   */
  @NonNull
  public MultiBoard remove(@NonNull Player player) {
    this.scoreboards.removeIf(
        scoreboard -> {
          if (scoreboard.getUniqueId().equals(player.getUniqueId())) {
            scoreboard.destroy();
            return true;
          }
          return false;
        });
    return this;
  }

  /**
   * Check if this board has a player.
   *
   * @param player the player to check if is inside the board
   * @return this same instance
   */
  public boolean contains(@NonNull Player player) {
    return this.scoreboards.stream()
        .anyMatch(board -> board.getUniqueId().equals(player.getUniqueId()));
  }
}
