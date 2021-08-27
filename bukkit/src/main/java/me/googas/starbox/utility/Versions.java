package me.googas.starbox.utility;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;

/** Utility class to get Bukkit version. */
public class Versions {

  @NonNull
  private static final List<Player> playerVersions =
      Arrays.asList(
          new Player(7, 0, 10),
          new Player(8, 47, 47),
          new Player(9, 107, 110),
          new Player(10, 210, 210),
          new Player(11, 315, 316),
          new Player(12, 335, 340),
          new Player(13, 393, 404),
          new Player(14, 477, 498),
          new Player(15, 573, 578),
          new Player(16, 735, 754),
          new Player(17, 755, 756));

  public static final int MIN_BUKKIT = 8;
  public static final int MAX_BUKKIT = 17;
  public static final String NMS = Bukkit.getServer().getClass().getCanonicalName().split("\\.")[3];
  public static int BUKKIT = Versions.check();

  /**
   * Check whats the bukkit version.
   *
   * @return the checked Bukkit version
   */
  public static int check() {
    String bukkitVersion = Bukkit.getBukkitVersion();
    for (int i = Versions.MIN_BUKKIT; i <= Versions.MAX_BUKKIT; i++) {
      if (bukkitVersion.contains("1." + i)) {
        Versions.BUKKIT = i;
        return i;
      }
    }
    return -1;
  }

  /**
   * Get the version of a player based on its protocol number.
   *
   * @param protocol the protocol number to get the version
   * @return the version of the player
   */
  public static Player getVersion(int protocol) {
    return Versions.playerVersions.stream()
        .filter(player -> protocol >= player.getMin() && protocol <= player.getMax())
        .findFirst()
        .orElseGet(Versions::getProtocol);
  }

  /**
   * Get the player version of the current 'Bukkit' version.
   *
   * @return the player version
   */
  public static Player getProtocol() {
    return Versions.playerVersions.stream()
        .filter(player -> player.getBukkit() == Versions.BUKKIT)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  /**
   * Represents the version of a {@link org.bukkit.entity.Player}. This includes the 'Bukkit'
   * version with matches with {@link #BUKKIT} and the minimum and maximum protocol number
   */
  public static class Player {

    @Getter private final int bukkit;
    @Getter private final int min;
    @Getter private final int max;

    private Player(int bukkit, int min, int max) {
      this.bukkit = bukkit;
      this.min = min;
      this.max = max;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
          .add("bukkit=" + bukkit)
          .add("min=" + min)
          .add("max=" + max)
          .toString();
    }
  }
}
