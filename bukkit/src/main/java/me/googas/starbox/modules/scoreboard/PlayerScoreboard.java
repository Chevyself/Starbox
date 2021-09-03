package me.googas.starbox.modules.scoreboard;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.Starbox;
import me.googas.starbox.Strings;
import me.googas.starbox.builders.MapBuilder;
import me.googas.starbox.utility.Versions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/** The custom scoreboard for {@link Player}. */
public class PlayerScoreboard {

  @NonNull
  private static final WrappedClass<Scoreboard> SCOREBOARD = WrappedClass.of(Scoreboard.class);

  @NonNull
  private static final WrappedMethod<Objective> REGISTER_NEW_OBJECTIVE =
      PlayerScoreboard.SCOREBOARD.getMethod(
          Objective.class, "registerNewObjective", String.class, String.class, String.class);

  @NonNull
  @APIVersion(since = 8, max = 13)
  private static final WrappedMethod<Objective> REGISTER_NEW_OBJECTIVE_NO_DISPLAY =
      PlayerScoreboard.SCOREBOARD.getMethod(
          Objective.class, "registerNewObjective", String.class, String.class);

  @NonNull
  private static final Map<Integer, String> characters =
      MapBuilder.of(10, "a")
          .put(11, "b")
          .put(12, "c")
          .put(13, "d")
          .put(14, "e")
          .put(15, "f")
          .build();

  @NonNull private final UUID player;
  @NonNull @Getter private final Scoreboard scoreboard;
  @NonNull @Getter private final Objective objective;
  @NonNull @Setter private List<ScoreboardLine> layout;

  private PlayerScoreboard(
      @NonNull UUID player,
      @NonNull Scoreboard scoreboard,
      @NonNull Objective objective,
      @NonNull List<ScoreboardLine> layout) {
    this.player = player;
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.layout = layout;
  }

  /**
   * Create the scoreboard for a player.
   *
   * @param player the player to create the scoreboard to
   * @param layout the layout of the scoreboard
   * @return the scoreboard
   */
  public static PlayerScoreboard create(
      @NonNull Player player, @NonNull List<ScoreboardLine> layout) {
    Scoreboard bukkitScoreboard =
        Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
    return new PlayerScoreboard(
        player.getUniqueId(),
        bukkitScoreboard,
        PlayerScoreboard.createObjective(bukkitScoreboard, player.getName(), "dummy", null),
        layout);
  }

  /**
   * This is just for creating the scoreboard nothing special. Empty string
   *
   * @param position the amount of spaces that the string should have
   * @return an empty string with spaces
   */
  @NonNull
  public static String getEntryName(int position) {
    return BukkitUtils.format(
        "&" + (position < 9 ? position : PlayerScoreboard.characters.get(position)) + "&r");
  }

  @NonNull
  private static Objective createObjective(
      @NonNull Scoreboard scoreboard,
      @NonNull String name,
      @NonNull String criteria,
      String display) {
    Objective objective;
    if (Versions.BUKKIT <= 13) {
      objective =
          PlayerScoreboard.REGISTER_NEW_OBJECTIVE_NO_DISPLAY
              .prepare(scoreboard, name, criteria)
              .handle(Starbox::severe)
              .provide()
              .orElse(null);
    } else {
      objective =
          PlayerScoreboard.REGISTER_NEW_OBJECTIVE
              .prepare(scoreboard, name, criteria, display == null ? name : display)
              .handle(Starbox::severe)
              .provide()
              .orElse(null);
    }
    return Objects.requireNonNull(objective, "Objective could not be created");
  }

  /**
   * Initialize the scoreboard. This will set the title and display slot also update the layout.
   *
   * @param title the title of the scoreboard if null it will be empty
   * @return this same instance
   */
  @NonNull
  public PlayerScoreboard initialize(String title) {
    this.objective.setDisplayName(title);
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    this.setNewLayout(this.layout);
    this.getPlayer().ifPresent(player -> player.setScoreboard(this.scoreboard));
    return this;
  }

  /**
   * Adds a new line to the scoreboard.
   *
   * @param line the line to be added
   * @return the created minecraft team
   */
  @NonNull
  private Team newLine(@NonNull ScoreboardLine line) {
    Team team = this.getLineTeam(line.getPosition());
    String entryName = PlayerScoreboard.getEntryName(line.getPosition());
    if (team == null) {
      team = this.scoreboard.registerNewTeam("line_" + line.getPosition());
      team.addEntry(entryName);
    }
    String current = team.getPrefix() + team.getDisplayName() + team.getSuffix();
    String build = line.build(this.getOfflinePlayer());
    if (current.equalsIgnoreCase(build)) return team;
    List<String> divide = Strings.divide(build, 16);
    String lastColor = "";
    for (int i = 0; i < divide.size(); i++) {
      String string = divide.get(i);
      switch (i) {
        case 0:
          team.setPrefix(lastColor + string);
          break;
        case 1:
          team.setSuffix(lastColor + string);
          break;
      }
      lastColor = ChatColor.getLastColors(string);
    }
    this.objective.getScore(entryName).setScore(line.getPosition());
    return team;
  }

  /**
   * Gets the line in a position.
   *
   * @param position the position to get the line
   * @return a minecraft team representing a line if it exists in the position
   */
  private Team getLineTeam(int position) {
    return this.scoreboard.getTeam("line_" + position);
  }

  private Optional<ScoreboardLine> getLine(int position) {
    return this.layout.stream().filter(line -> line.getPosition() == position).findFirst();
  }

  /** Updates the scoreboard. */
  public void update() {
    this.layout.forEach(this::newLine);
  }

  /**
   * Updates the line in the given position.
   *
   * @param position the position of the line to update
   */
  public void update(int position) {
    this.getLine(position).ifPresent(this::newLine);
  }

  @NonNull
  private Optional<Player> getPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.player));
  }

  @NonNull
  private OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(this.player);
  }

  /**
   * Get the unique id of the player in this scoreboard.
   *
   * @return the unique id
   */
  public UUID getUniqueId() {
    return player;
  }

  /** Destroy this scoreboard. */
  public void destroy() {
    this.scoreboard.getTeams().forEach(Team::unregister);
  }

  /**
   * Sets the new layout of the scoreboard.
   *
   * @param layout the new layout
   */
  private void setNewLayout(@NonNull List<ScoreboardLine> layout) {
    this.layout = layout;
    Set<Team> editedTeams = new HashSet<>();
    layout.forEach(line -> editedTeams.add(this.newLine(line)));
    this.scoreboard.getTeams().stream()
        .filter(team -> !editedTeams.contains(team))
        .forEach(Team::unregister);
    this.update();
  }

  private void setTitle(@NonNull String title) {
    this.objective.setDisplayName(title);
  }
}
