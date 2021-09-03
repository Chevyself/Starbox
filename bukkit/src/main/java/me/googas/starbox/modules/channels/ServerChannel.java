package me.googas.starbox.modules.channels;

import java.util.Locale;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.wrappers.chat.WrappedSoundCategory;
import me.googas.starbox.utility.Versions;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;

/** This channel send a message to all the server including the console. */
public class ServerChannel implements Channel {

  @NonNull @Getter public static final ServerChannel instance = new ServerChannel();

  private ServerChannel() {}

  @Override
  public @NonNull ServerChannel send(@NonNull BaseComponent... components) {
    Bukkit.getOnlinePlayers().stream()
        .map(Channel::of)
        .forEach(channel -> channel.send(components));
    ConsoleChannel.getInstance().send(components);
    return this;
  }

  @Override
  public @NonNull ServerChannel send(@NonNull String text) {
    Bukkit.getOnlinePlayers().stream().map(Channel::of).forEach(channel -> channel.send(text));
    ConsoleChannel.getInstance().send(text);
    return this;
  }

  @Override
  public @NonNull ServerChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    Bukkit.getOnlinePlayers().stream()
        .map(Channel::of)
        .forEach(channel -> channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    ConsoleChannel.getInstance().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    return this;
  }

  @Override
  public @NonNull ServerChannel setTabList(String header, String bottom) {
    return this;
  }

  @Override
  public @NonNull Channel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    Bukkit.getWorlds()
        .forEach(
            world -> {
              if (Versions.BUKKIT < 11 || !category.get().isPresent()) {
                world.playSound(location, sound, volume, pitch);
              } else {
                world.playSound(location, sound, category.get().get(), volume, pitch);
              }
            });
    return this;
  }

  @Override
  public @NonNull Channel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    Bukkit.getWorlds().forEach(world -> world.playSound(location, sound, volume, pitch));
    return this;
  }

  @Override
  public Optional<Locale> getLocale() {
    return Optional.empty();
  }
}
