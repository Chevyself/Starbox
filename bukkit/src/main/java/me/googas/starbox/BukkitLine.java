package me.googas.starbox;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.bungee.utils.Components;
import me.googas.starbox.builders.Line;
import me.googas.starbox.modules.channels.Channel;
import me.googas.starbox.modules.channels.ForwardingChannel;
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
        locale, Starbox.getModules().require(LanguageModule.class).getRaw(locale, key).trim());
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
   * Get the localized lines for a forwarding channel.
   *
   * @param forwardingChannel the forwarding channel to get the lines
   * @param key the key to get the json/text message
   * @return a {@link List} containing the lines
   */
  static List<Localized> localized(
      @NonNull ForwardingChannel.Multiple forwardingChannel, @NonNull String key) {
    return forwardingChannel.getChannels().stream()
        .map(channel -> BukkitLine.localized(channel, key))
        .collect(Collectors.toList());
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
   * Parse a line from a string. If the string starts with 'localized:' a {@link Localized} will be
   * returned else a {@link Plain} will be provided
   *
   * @param locale the locale parsing the line
   * @param string the string to parse
   * @return the parsed line
   */
  static BukkitLine parse(Locale locale, @NonNull String string) {
    if (string.startsWith("localized:") && locale != null) {
      return BukkitLine.localized(locale, string.substring(10));
    } else {
      return BukkitLine.of(string);
    }
  }

  /**
   * Build the line as a {@link Result} for commands.
   *
   * @return the line built as a result
   */
  @NonNull
  Result asResult();

  @Override
  BaseComponent @NonNull [] build();

  /**
   * Set the raw text of the line.
   *
   * @see #getRaw()
   * @param raw the new raw text
   * @return this same instance
   */
  @NonNull
  Line setRaw(@NonNull String raw);

  /**
   * Get the raw text of the line. This is the line without being formatted.
   *
   * Ex: {@link Localized} the raw text is its json
   *
   * @return the raw text
   */
  @NonNull
  String getRaw();

  /**
   * This must be used if the line is a sample line to format it. This line will be formatted using
   * {@link me.googas.starbox.modules.language.SampleFormatter}
   *
   * @return this same instance
   */
  @NonNull
  default BukkitLine formatSample() {
    Starbox.getModules().get(LanguageModule.class).ifPresent(module -> this.format(module.getSampleFormatter()));
    return this;
  }

  /** This is a {@link BukkitLine} which uses a message obtained from {@link LanguageModule}. */
  class Localized implements BukkitLine {

    @NonNull @Getter private final Locale locale;
    @NonNull private String json;

    private Localized(@NonNull Locale locale, @NonNull String json) {
      this.locale = locale;
      this.json = jso
s;
    }

    @Override
    public @NonNull String getRaw() {
      return thrn thi
n;
    }

    @NonNull
    public Localized setRaw(@NonNull String json) {
      this.json = json;
      retuis.jso
n;
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
    public @NonNull String getRaw() {
      return this.text;
    }

    @
Override
    public @NonNull Plain setRaw(@NonNull String raw) {
      this.text = raw;
      return this;
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
