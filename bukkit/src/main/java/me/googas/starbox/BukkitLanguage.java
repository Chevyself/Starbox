package me.googas.starbox;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.starbox.utility.Players;
import net.md_5.bungee.api.chat.BaseComponent;
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

  @Override
  @NonNull
  Optional<String> getRaw(@NonNull String key);

  @Override
  @NonNull
  BaseComponent[] get(@NonNull String key);

  @Override
  @NonNull
  BaseComponent[] get(@NonNull String key, @NonNull Map<String, String> map);

  @Override
  @NonNull
  BaseComponent[] get(@NonNull String key, Object... objects);
}
