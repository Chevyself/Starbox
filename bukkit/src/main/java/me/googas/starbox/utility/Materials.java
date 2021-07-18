package me.googas.starbox.utility;

import lombok.NonNull;
import org.bukkit.Material;

public class Materials {

  public static boolean isBanner(@NonNull Material material) {
    return material.name().endsWith("BANNER");
  }

  public static boolean isWool(@NonNull Material material) {
    return material.name().equals("WOOL");
  }

  public static boolean isSkull(@NonNull Material material) {
    return material == Materials.getSkull();
  }

  public static boolean isLog(@NonNull Material material) {
    return material.name().endsWith("LOG");
  }

  public static boolean isLeaves(@NonNull Material material) {
    return material.name().endsWith("LEAVES");
  }

  public static boolean isTool(Material material) {
    String name = material.name();
    return name.endsWith("AXE")
        || name.endsWith("HOE")
        || name.endsWith("SWORD")
        || name.endsWith("PICKAXE")
        || name.endsWith("SHOVEL")
        || name.endsWith("SPADE");
  }

  public static boolean isAxe(Material material) {
    return material.name().endsWith("AXE");
  }

  public static Material getSkull() {
    if (Versions.BUKKIT > 12) {
      return Material.getMaterial("PLAYER_HEAD");
    } else {
      return Material.getMaterial("SKULL_ITEM");
    }
  }
}
