package me.googas.starbox;

import lombok.NonNull;

/**
 * Static utilities for JSON.
 *
 * <p>Used mostly for Bukkit and Bungee BaseComponents
 */
public class JsonUtils {

  /**
   * Checks if a string is json. This method will return true if the string starts with "[" or "{"
   * and ends with "]" or "}"
   *
   * @param string the string to check
   * @return true if the string is json
   */
  public static boolean isJson(@NonNull String string) {
    return string.startsWith("{") && string.endsWith("}")
        || string.startsWith("[") && string.endsWith("]");
  }
}
