package me.googas.starbox.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.reflect.wrappers.attributes.WrappedAttribute;
import me.googas.reflect.wrappers.attributes.WrappedAttributeInstance;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/** Utility for {@link Player}. */
public class Players {

  @NonNull public static final WrappedClass PLAYER = WrappedClass.of(Player.class);
  @NonNull public static final WrappedClass SPIGOT_PLAYER = WrappedClass.of(Player.Spigot.class);

  @NonNull
  public static final WrappedClass PLAYER_INVENTORY = WrappedClass.of(PlayerInventory.class);

  @APIVersion(since = 8)
  public static final WrappedMethod<String> SPIGOT_GET_LANG =
      Players.SPIGOT_PLAYER.getMethod(String.class, "getLang");

  @APIVersion(since = 8)
  public static final WrappedMethod<Double> GET_MAX_HEALTH =
      Players.PLAYER.getMethod(Double.class, "getMaxHealth");

  @APIVersion(since = 8)
  public static final WrappedMethod<ItemStack> GET_ITEM_IN_HAND =
      Players.PLAYER.getMethod(ItemStack.class, "getItemInHand");

  @APIVersion(since = 9)
  public static final WrappedMethod<ItemStack> GET_ITEM_IN_MAIN_HAND =
      Players.PLAYER_INVENTORY.getMethod(ItemStack.class, "getItemInMainHand");

  @APIVersion(since = 9, max = 11)
  public static final WrappedMethod<String> SPIGOT_GET_LOCALE =
      Players.SPIGOT_PLAYER.getMethod(String.class, "getLocale");

  /**
   * Get the locale language of a players game.
   *
   * @param player the player to get its locale language
   * @return the locale language of a player
   */
  @NonNull
  public static String getLocale(@NonNull Player player) {
    switch (Versions.BUKKIT) {
      case 8:
        return Players.SPIGOT_GET_LANG.prepare(player.spigot()).provide().orElse("en");
      case 9:
      case 10:
      case 11:
        return Players.SPIGOT_GET_LOCALE.prepare(player.spigot()).provide().orElse("en");
      default:
        return player.getLocale();
    }
  }

  /**
   * Reset a player. This will set its max health and saturation reduce its levels to 0 and remove
   * the ability to pick up items
   *
   * @param player the player to reset
   * @param gameMode the gamemode to set the player to
   */
  public static void reset(@NonNull Player player, @NonNull GameMode gameMode) {
    Players.setHealthToMax(player);
    player.setSaturation(25);
    player.setExp(0);
    player.setTotalExperience(0);
    player.setFoodLevel(25);
    player.setLevel(0);
    player.setCanPickupItems(false); // ?
    player.setGameMode(gameMode);
  }

  /**
   * Reset a player and set its gamemode to {@link GameMode#SURVIVAL}.
   *
   * @param player the player to reset
   */
  public static void reset(@NonNull Player player) {
    Players.reset(player, GameMode.SURVIVAL);
  }

  /**
   * Get the attribute instance of a player.
   *
   * @param player the player to get the attribute
   * @param attribute the attribute to get it instance
   * @return the attribute instance if bukkit version is above 8 else null
   */
  @NonNull
  public static WrappedAttributeInstance getAttribute(
      @NonNull Player player, @NonNull WrappedAttribute attribute) {
    return new WrappedAttributeInstance(
        Versions.BUKKIT == 8 ? null : player.getAttribute(attribute.getAttribute()));
  }

  /**
   * Get the item in hand of a player.
   *
   * @param player the player to get its item in hand
   * @return the item in hand of the player
   */
  @NonNull
  public static Optional<ItemStack> getItemInMainHand(@NonNull Player player) {
    if (Versions.BUKKIT < 9) {
      return Players.GET_ITEM_IN_HAND.prepare(player).provide();
    } else {
      return Players.GET_ITEM_IN_MAIN_HAND.prepare(player.getInventory()).provide();
    }
  }

  /**
   * Set the health of a player to the maximum.
   *
   * @param player the player to set the health to maximum
   */
  public static void setHealthToMax(@NonNull Player player) {
    double maxHealth;
    if (Versions.BUKKIT < 9) {
      maxHealth = Players.GET_MAX_HEALTH.prepare(player).provide().orElse(20D);
    } else {
      WrappedAttribute genericMaxHealth = WrappedAttribute.valueOf("GENERIC_MAX_HEALTH");
      WrappedAttributeInstance attribute = Players.getAttribute(player, genericMaxHealth);
      // Using 20 because that's default?
      maxHealth = attribute.isPresent() ? attribute.getBaseValue() : 20;
    }
    player.setHealth(maxHealth);
  }

  /**
   * Get the names of all the players online.
   *
   * @return the names of the online players.
   */
  @NonNull
  public static List<String> getOnlinePlayersNames() {
    List<String> names = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      names.add(player.getName());
    }
    return names;
  }
}
