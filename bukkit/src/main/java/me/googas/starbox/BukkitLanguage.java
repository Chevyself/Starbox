package me.googas.starbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.Result;
import me.googas.starbox.utility.Players;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** Extension for {@link Language} to use in 'Bukkit'. */
public interface BukkitLanguage extends Language {

  @NonNull List<BukkitLanguage> languages = new ArrayList<>();

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
   * Get a bukkit language from a locale.
   *
   * @param locale the locale to get the bukkit language for
   * @return the bukkit language if no matching is found {@link Locale#ENGLISH} will be used
   */
  @NonNull
  static BukkitLanguage getLanguage(@NonNull Locale locale) {
    return BukkitLanguage.languages.stream()
        .filter(
            language -> language.getLocale().getLanguage().equalsIgnoreCase(locale.getLanguage()))
        .findFirst()
        .orElseGet(BukkitLanguage::getDefault);
  }

  /**
   * Builds a simple help message appending children commands for the parent.
   *
   * @param command the command to build the help for
   * @param context the context of the command execution
   * @return the base components
   */
  @NonNull
  BaseComponent[] buildHelp(@NonNull StarboxBukkitCommand command, @NonNull CommandContext context);

  /**
   * Get a bukkit language from a command sender.
   *
   * @param sender the sender to get the language for
   * @return the language
   */
  @NonNull
  static BukkitLanguage getLanguage(@NonNull CommandSender sender) {
    return BukkitLanguage.getLanguage(BukkitLanguage.getLocale(sender));
  }

  /**
   * Get an object as a result.
   *
   * @param sender the sender to get the locale
   * @param key the key to get the object
   * @return the object as a result
   */
  static Result asResult(@NonNull CommandSender sender, @NonNull String key) {
    return new Result(BukkitLanguage.getLanguage(sender).get(key));
  }

  /**
   * Get an object as a result.
   *
   * @param sender the sender to get the locale
   * @param key the key to get the object
   * @param map a map to format the object
   * @return the object as a result
   */
  static Result asResult(
      @NonNull CommandSender sender, @NonNull String key, @NonNull Map<String, String> map) {
    return new Result(BukkitLanguage.getLanguage(sender).get(key, map));
  }

  /**
   * Get an object as a result.
   *
   * @param sender the sender to get the locale
   * @param key the key to get the object
   * @param object to format the object
   * @return the object as a result
   */
  static Result asResult(
      @NonNull CommandSender sender, @NonNull String key, @NonNull Object... object) {
    return new Result(BukkitLanguage.getLanguage(sender).get(key, object));
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

  /**
   * Get the default language of the plugin.
   *
   * @return the default language
   */
  @NonNull
  static BukkitLanguage getDefault() {
    return BukkitLanguage.getLanguage(Locale.ENGLISH);
  }
}
