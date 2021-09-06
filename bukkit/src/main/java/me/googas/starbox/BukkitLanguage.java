package me.googas.starbox;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.starbox.utility.Players;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** Extension for {@link Language} to use in 'Bukkit'. */
public interface BukkitLanguage extends Language {

  /**
   * Get the locale from a {@link CommandSender}.
   *
   * @param sender the sender to get the locale
   * @return the locale of the sender
   */
  @NonNull
  static Locale getLocale(@NonNull CommandSender sender) {
    if (sender instanceof Player) {
      return Language.getLocale(Players.getLocale((Player) sender));
    } else {
      return Locale.ENGLISH;
    }
  }

  /**
   * Get the locale from a {@link OfflinePlayer}.
   *
   * @param player the player to get the locale from
   * @return the locale
   */
  static Locale getOfflineLocale(@NonNull OfflinePlayer player) {
    Player online = player.getPlayer();
    return online != null ? BukkitLanguage.getLocale(online) : Locale.ENGLISH;
  }

  @Override
  @NonNull
  Optional<String> getRaw(@NonNull String key);

  @Override
  BaseComponent @NonNull [] get(@NonNull String key);

  @Override
  BaseComponent @NonNull [] get(@NonNull String key, @NonNull Map<String, String> map);

  @Override
  @NonNull
  BaseComponent @NonNull [] get(@NonNull String key, Object... objects);

  /**
   * Whether the language represents a sample language.
   *
   * @return true if the language contains sample keys
   */
  boolean isSample();
}
