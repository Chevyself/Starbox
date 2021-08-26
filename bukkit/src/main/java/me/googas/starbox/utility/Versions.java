package me.googas.starbox.utility;

import org.bukkit.Bukkit;

/** Utility class to get Bukkit version. */
public class Versions {

  public static final int MIN_BUKKIT = 8;
  public static final int MAX_BUKKIT = 17;
  public static int BUKKIT = Versions.check();
  public static String NMS = Bukkit.getServer().getClass().getCanonicalName().split("\\.")[3];

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
}
