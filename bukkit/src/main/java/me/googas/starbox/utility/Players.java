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

public class Players {

  @NonNull public static WrappedClass PLAYER = WrappedClass.of(Player.class);
  @NonNull public static WrappedClass SPIGOT_PLAYER = WrappedClass.of(Player.Spigot.class);
  @NonNull public static WrappedClass PLAYER_INVENTORY = WrappedClass.of(PlayerInventory.class);

  @APIVersion(value = 8)
  public static WrappedMethod<String> SPIGOT_GET_LANG =
      Players.SPIGOT_PLAYER.getMethod(String.class, "getLang");

  @APIVersion(value = 9)
  public static WrappedMethod<?> GET_ATTRIBUTE =
      Players.PLAYER.getMethod("getAttribute", WrappedAttribute.ATTRIBUTE.getClazz());

  @APIVersion(value = 8)
  public static WrappedMethod<?> GET_MAX_HEALTH = Players.PLAYER.getMethod("getMaxHealth");

  @APIVersion(value = 8)
  public static WrappedMethod<ItemStack> GET_ITEM_IN_HAND =
      Players.PLAYER.getMethod(ItemStack.class, "getItemInHand");

  @APIVersion(value = 9)
  public static WrappedMethod<ItemStack> GET_ITEM_IN_MAIN_HAND =
      Players.PLAYER_INVENTORY.getMethod(ItemStack.class, "getItemInMainHand");

  @APIVersion(value = 9, max = 11)
  public static WrappedMethod<String> SPIGOT_GET_LOCALE =
      Players.SPIGOT_PLAYER.getMethod(String.class, "getLocale");

  @APIVersion(12)
  public static WrappedMethod<String> GET_LOCALE =
      Players.PLAYER.getMethod(String.class, "getLocale");

  @NonNull
  public static String getLocale(@NonNull Player player) {
    switch (Versions.BUKKIT) {
      case 8:
        return Players.SPIGOT_GET_LANG.invoke(player.spigot()).provide().orElse("en");
      case 9:
      case 10:
      case 11:
        return Players.SPIGOT_GET_LOCALE.invoke(player.spigot()).provide().orElse("en");
      default:
        return Players.GET_LOCALE.invoke(player).provide().orElse("en");
    }
  }

  public static void reset(@NonNull Player player, @NonNull GameMode gameMode) {
    Players.setHealthToMax(player);
    player.setSaturation(25);
    player.setExp(0);
    player.setTotalExperience(0);
    player.setFoodLevel(25);
    player.setLevel(0);
    player.setCanPickupItems(false);
    player.setGameMode(gameMode);
  }

  public static void reset(@NonNull Player player) {
    Players.reset(player, GameMode.SURVIVAL);
  }

  public static WrappedAttributeInstance getAttribute(
      @NonNull Player player, @NonNull WrappedAttribute attribute) {
    if (Versions.BUKKIT == 8) return null;
    Object invoke = Players.GET_ATTRIBUTE.invoke(player, attribute.toAttribute());
    if (invoke != null) {
      return new WrappedAttributeInstance(invoke);
    }
    return null;
  }

  @NonNull
  public static Optional<ItemStack> getItemInMainHand(@NonNull Player player) {
    if (Versions.BUKKIT < 9) {
      return Players.GET_ITEM_IN_HAND.invoke(player).provide();
    } else {
      return Players.GET_ITEM_IN_MAIN_HAND.invoke(player.getInventory()).provide();
    }
  }

  public static void setHealthToMax(@NonNull Player player) {
    double maxHealth = 20;
    if (Versions.BUKKIT < 9) {
      Object invoke = Players.GET_MAX_HEALTH.invoke(player);
      if (invoke instanceof Double) {
        maxHealth = (double) invoke;
      }
    } else {
      WrappedAttributeInstance attribute =
          Players.getAttribute(player, WrappedAttribute.GENERIC_MAX_HEALTH);
      if (attribute != null) {
        maxHealth = attribute.getBaseValue();
      }
    }
    player.setHealth(maxHealth);
  }

  @NonNull
  public static List<String> getOnlinePlayersNames() {
    List<String> names = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      names.add(player.getName());
    }
    return names;
  }
}
