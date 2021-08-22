package me.googas.starbox.modules.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import me.googas.starbox.BukkitLanguage;
import me.googas.starbox.modules.Module;
import org.bukkit.plugin.Plugin;

/**
 * This module is used to register {@link BukkitLanguage} and start localized messages. Each {@link
 * Plugin} has its own separated messages but for instance to create a {@link
 * me.googas.starbox.BukkitLine.Localized} it will not matter, that is why each language should have
 * its own unique keys
 */
public class LanguageModule implements Module {

  @NonNull private final Map<Plugin, List<BukkitLanguage>> languages = new HashMap<>();

  /**
   * Register a {@link BukkitLanguage}.
   *
   * @param plugin the plugin that is registering the language
   * @param language the language that is being registered
   * @return this same instance
   */
  public LanguageModule register(@NonNull Plugin plugin, @NonNull BukkitLanguage language) {
    this.languages.computeIfAbsent(plugin, pluginKey -> new ArrayList<>()).add(language);
    return this;
  }

  /**
   * Register many {@link BukkitLanguage}.
   *
   * @param plugin the plugin that is registering the languages
   * @param languages the language that is being registered
   * @return this same instance
   */
  public LanguageModule registerAll(
      @NonNull Plugin plugin, @NonNull Collection<BukkitLanguage> languages) {
    this.languages.computeIfAbsent(plugin, pluginKey -> new ArrayList<>()).addAll(languages);
    return this;
  }

  /**
   * Unregisters all languages from a {@link Plugin}.
   *
   * @param plugin the plugin to unregister the languages
   * @return this same instance
   */
  public LanguageModule unregister(@NonNull Plugin plugin) {
    this.languages.remove(plugin);
    return this;
  }

  /**
   * Get a bukkit languages from a locale.
   *
   * @param locale the locale to get the bukkit languages for
   * @return the bukkit languages
   */
  @NonNull
  public List<BukkitLanguage> getLanguages(@NonNull Locale locale) {
    List<BukkitLanguage> matching = new ArrayList<>();
    this.languages
        .values()
        .forEach(
            list ->
                matching.addAll(
                    list.stream()
                        .filter(
                            language ->
                                language
                                    .getLocale()
                                    .getLanguage()
                                    .equalsIgnoreCase(locale.getLanguage()))
                        .collect(Collectors.toList())));
    return matching;
  }

  /**
   * Get the raw message from a key. If no message is found for the 'key' the same 'key' will be
   * returned
   *
   * @param locale the locale to get the language
   * @param key the key to get the raw message
   * @return the raw message
   */
  @NonNull
  public String getRaw(@NonNull Locale locale, @NonNull String key) {
    return this.getLanguages(locale).stream()
        .map(
            language -> {
              Optional<String> raw = language.getRaw(key);
              return language.getRaw(key).orElse(null);
            })
        .findFirst()
        .orElse(key);
  }

  @Override
  public void onDisable() {
    this.languages.clear();
    Module.super.onDisable();
  }

  @Override
  public @NonNull String getName() {
    return "Language";
  }
}
