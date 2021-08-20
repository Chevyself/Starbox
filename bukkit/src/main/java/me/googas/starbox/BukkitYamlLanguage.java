package me.googas.starbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.reflect.wrappers.chat.AbstractComponentBuilder;
import me.googas.starbox.builders.MapBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/** Represents a {@link BukkitLanguage} at a '.yml' file. */
public class BukkitYamlLanguage implements BukkitLanguage {

  @NonNull @Getter private final Locale locale;
  @NonNull private final ConfigurationSection section;

  private BukkitYamlLanguage(@NonNull Locale locale, @NonNull ConfigurationSection section) {
    this.locale = locale;
    this.section = section;
  }

  /**
   * Get a language from a reader.
   *
   * @param reader the reader to read the language
   * @return the read language
   */
  @NonNull
  public static BukkitYamlLanguage of(@NonNull Reader reader) {
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
    return new BukkitYamlLanguage(
        new Locale(
            Objects.requireNonNull(
                configuration.getString("language"), "There's no 'language' field")),
        configuration);
  }

  /**
   * Get a language from a {@link InputStream}. This will create a reader and use it in {@link
   * #of(Reader)}
   *
   * @param resource the stream to start the reader
   * @return the read yaml language
   */
  @NonNull
  public static BukkitYamlLanguage of(@NonNull InputStream resource) {
    InputStreamReader reader = new InputStreamReader(resource);
    BukkitYamlLanguage language = BukkitYamlLanguage.of(reader);
    try {
      reader.close();
    } catch (IOException e) {
      Starbox.warning(e, () -> "Reader was not closed successfully");
    }
    return language;
  }

  @NonNull
  public Optional<String> getRaw(@NonNull String key) {
    return Optional.ofNullable(section.getString(key));
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key) {
    return BukkitUtils.getComponent(BukkitUtils.format(this.getRaw(key).orElse(key).trim()));
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key, @NonNull Map<String, String> map) {
    return BukkitUtils.getComponent(BukkitUtils.format(this.getRaw(key).orElse(key), map).trim());
  }

  @NonNull
  public BaseComponent[] get(@NonNull String key, Object... objects) {
    return BukkitUtils.getComponent(
        BukkitUtils.format(this.getRaw(key).orElse(key), objects).trim());
  }

  @Override
  public BaseComponent[] buildHelp(@NonNull StarboxBukkitCommand command, @NonNull CommandContext context) {
    BukkitLanguage language = BukkitLanguage.getLanguage(context.getSender());
    AbstractComponentBuilder builder =
            new AbstractComponentBuilder(
                    language.get("subcommands.title"));
    List<StarboxBukkitCommand> children = command.getChildren()
            .stream()
            .filter(child -> child.getPermission() == null || context.getSender().hasPermission(child.getPermission())).collect(Collectors.toList());
    if (children.isEmpty()) {
      builder.append("subcommands.empty-children");
    } else {
      children.forEach(child -> {
        builder.appendAll(ComponentBuilder.FormatRetention.NONE, language.get("subcommands.child", MapBuilder.of("parent", "itemBuilder")
                .put("children", child.getName())
                .put("description", child.getDescription())
                .build()));
      });
    }
    return builder.build();
  }
}
