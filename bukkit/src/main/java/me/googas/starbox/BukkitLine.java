package me.googas.starbox;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bungee.utils.Components;
import me.googas.starbox.builders.Line;
import me.googas.starbox.modules.channels.Channel;
import me.googas.starbox.modules.language.LanguageModule;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

/** Implementation of {@link Line} to be used in 'Bukkit'. */
public interface BukkitLine extends Line {

  /**
   * Start a localized line.
   *
   * @param locale the locale to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  @NonNull
  static Localized localized(@NonNull Locale locale, @NonNull String key) {
    return new Localized(
        Starbox.getModules().require(LanguageModule.class).getRaw(locale, key).trim());
  }

  /**
   * Start a localized line.
   *
   * @param sender the sender to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  @NonNull
  static Localized localized(@NonNull CommandSender sender, @NonNull String key) {
    return BukkitLine.localized(BukkitLanguage.getLocale(sender), key);
  }

  /**
   * Start a localized line.
   *
   * @param channel the channel to get the language
   * @param key the key to get the json/text message
   * @return a new {@link Localized} instance
   */
  static Localized localized(@NonNull Channel channel, String key) {
    return BukkitLine.localized(channel.getLocale().orElse(Locale.ENGLISH), key);
  }

  /**
   * Start a plain line.
   *
   * @param text the text of the line
   * @return a plain line
   */
  static Plain of(@NonNull String text) {
    return new BukkitLine.Plain(text);
  }

  /**
   * Build the line as a {@link Result} for commands.
   *
   * @return the line built as a result
   */
  @NonNull
  Result asResult();

  @NonNull
  @Override
  BaseComponent[] build();

  /** This is a {@link BukkitLine} which uses a message obtained from {@link LanguageModule}. */
  class Localized implements BukkitLine {

    @NonNull @Getter private String json;

    private Localized(@NonNull String json) {
      this.json = json;
    }

    /**
     * Set the message json to build the components.
     *
     * @param json the text to be set, it does not have to be json
     * @return this same instance
     */
    @NonNull
    public Localized setJson(@NonNull String json) {
      this.json = json;
      return this;
    }

    @Override
    public @NonNull Result asResult() {
      return new Result(this.build());
    }

    @Override
    public @NonNull BaseComponent[] build() {
      return Components.getComponent(json);
    }

    @Override
    public @NonNull Optional<String> asText() {
      return Optional.of(new TextComponent(this.build()).toLegacyText());
    }

    @Override
    public @NonNull Localized format(@NonNull Object... objects) {
      json = Strings.format(json, objects);
      return this;
    }

    @Override
    public @NonNull Localized format(@NonNull Map<String, String> map) {
      json = Strings.format(json, map);
      return this;
    }

    @Override
    public @NonNull Localized format(@NonNull Formatter formatter) {
      return (Localized) formatter.format(this);
    }
  }

  /** Represents a plain text line. */
  class Plain implements BukkitLine {

    @NonNull private String text;

    private Plain(@NonNull String text) {
      this.text = text;
    }

    @Override
    public @NonNull Result asResult() {
      return new Result(this.build());
    }

    @Override
    public @NonNull BaseComponent[] build() {
      return Components.deserializePlain('&', text);
    }

    @Override
    public @NonNull Optional<String> asText() {
      return Optional.of(BukkitUtils.format(text));
    }

    @Override
    public @NonNull Plain format(@NonNull Object... objects) {
      this.text = String.format(text, objects);
      return this;
    }

    @Override
    public @NonNull Plain format(@NonNull Map<String, String> map) {
      this.text = String.format(text, map);
      return this;
    }

    @Override
    public @NonNull Plain format(@NonNull Formatter formatter) {
      formatter.format(this);
      return this;
    }
  }
}
