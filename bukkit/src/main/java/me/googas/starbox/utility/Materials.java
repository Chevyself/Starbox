package me.googas.starbox.utility;

import lombok.NonNull;
import org.bukkit.Material;

/** Utility class for {@link Material} */
public class Materials {

  /**
   * Check whether a material is a banner.
   *
   * @param material the material to check
   * @return true if the material is a banner
   */
  public static boolean isBanner(@NonNull Material material) {
    return material.name().endsWith("BANNER");
  }

  /**
   * Check whether a material is wool.
   *
   * @param material the material to check
   * @return true if the material is wool
   */
  public static boolean isWool(@NonNull Material material) {
    return material.name().equals("WOOL");
  }

  /**
   * Check whether a material is a skull.
   *
   * @param material the material to check
   * @return true if the material is a skull
   */
  public static boolean isSkull(@NonNull Material material) {
    return material == Materials.getSkull();
  }

  /**
   * Check whether a material is a log of wood.
   *
   * @param material the material to check
   * @return true if the material is a log of wood.
   */
  public static boolean isLog(@NonNull Material material) {
    return material.name().endsWith("LOG");
  }

  /**
   * Check whether a material are leaves.
   *
   * @param material the material to check
   * @return true if the material are leaves
   */
  public static boolean isLeaves(@NonNull Material material) {
    return material.name().endsWith("LEAVES");
  }

  /**
   * Check whether the material is a tool.
   *
   * @param material the material to check
   * @return true if the material is a tool
   */
  public static boolean isTool(@NonNull Material material) {
    String name = material.name();
    // TODO add shears
    return name.endsWith("AXE")
        || name.endsWith("HOE")
        || name.endsWith("SWORD")
        || name.endsWith("PICKAXE")
        || name.endsWith("SHOVEL")
        || name.endsWith("SPADE");
  }

  /**
   * Check whether a material is an axe.
   *
   * @param material the material to check
   * @return true if the material is an axe
   */
  public static boolean isAxe(@NonNull Material material) {
    return material.name().endsWith("AXE");
  }

  /**
   * Get the material that represents a skull.
   *
   * @return the skull material
   */
  @NonNull
  public static Material getSkull() {
    if (Versions.BUKKIT > 12) {
      return Material.getMaterial("PLAYER_HEAD");
    } else {
      return Material.getMaterial("SKULL_ITEM");
    }
  }
}
