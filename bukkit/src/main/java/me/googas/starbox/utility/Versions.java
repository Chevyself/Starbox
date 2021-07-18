package me.googas.starbox.utility;

import org.bukkit.Bukkit;

public class Versions {

  public static int MIN_BUKKIT = 8;
  public static int MAX_BUKKIT = 17;
  public static int BUKKIT = Versions.check();

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
