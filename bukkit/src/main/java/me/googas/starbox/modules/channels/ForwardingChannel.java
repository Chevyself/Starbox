package me.googas.starbox.modules.channels;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.reflect.wrappers.chat.WrappedSoundCategory;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.Sound;

/** A forwarding channel is a channel which wraps another channel to send data. */
public interface ForwardingChannel extends Channel {

  /**
   * Get the channel that is being forwarded.
   *
   * @return a {@link Optional} holding the nullable channel
   */
  @NonNull
  Optional<Channel> getForward();

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull BaseComponent... components) {
    this.getForward().ifPresent(channel -> channel.send(components));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel send(@NonNull String text) {
    this.getForward().ifPresent(channel -> channel.send(text));
    return this;
  }

  @Override
  default Optional<Locale> getLocale() {
    return this.getForward().flatMap(Channel::getLocale);
  }

  @Override
  @NonNull
  default ForwardingChannel sendTitle(
      String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    this.getForward()
        .ifPresent(channel -> channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel setTabList(String header, String bottom) {
    this.getForward().ifPresent(channel -> channel.setTabList(header, bottom));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel playSound(
      @NonNull Location location,
      @NonNull Sound sound,
      @NonNull WrappedSoundCategory category,
      float volume,
      float pitch) {
    this.getForward()
        .ifPresent(channel -> channel.playSound(location, sound, category, volume, pitch));
    return this;
  }

  @Override
  @NonNull
  default ForwardingChannel playSound(
      @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
    this.getForward().ifPresent(channel -> channel.playSound(location, sound, volume, pitch));
    return this;
  }

  /** This type of forwarding channel wraps more than one channel. */
  interface Multiple extends Channel {

    /**
     * Get all the wrapped channels.
     *
     * @return this same instance
     */
    @NonNull
    Collection<? extends Channel> getChannels();

    @Override
    @NonNull
    default Multiple send(@NonNull BaseComponent... components) {
      this.getChannels().forEach(channel -> channel.send(components));
      return this;
    }

    @Override
    @NonNull
    default Multiple send(@NonNull String text) {
      this.getChannels().forEach(channel -> channel.send(text));
      return this;
    }

    @Override
    @NonNull
    default Multiple localized(@NonNull String key) {
      this.getChannels().forEach(channel -> channel.localized(key));
      return this;
    }

    @Override
    @NonNull
    default Multiple localized(@NonNull String key, @NonNull Map<String, String> map) {
      this.getChannels().forEach(channel -> channel.localized(key, map));
      return this;
    }

    @Override
    @NonNull
    default Multiple localized(@NonNull String key, @NonNull Object... objects) {
      this.getChannels().forEach(channel -> channel.localized(key, objects));
      return this;
    }

    @Override
    default Optional<Locale> getLocale() {
      return Optional.empty();
    }

    @Override
    @NonNull
    default Channel playSound(
        @NonNull Location location,
        @NonNull Sound sound,
        @NonNull WrappedSoundCategory category,
        float volume,
        float pitch) {
      this.getChannels()
          .forEach(channel -> channel.playSound(location, sound, category, volume, pitch));
      return this;
    }

    @Override
    @NonNull
    default Channel playSound(
        @NonNull Location location, @NonNull Sound sound, float volume, float pitch) {
      this.getChannels().forEach(channel -> channel.playSound(location, sound, volume, pitch));
      return this;
    }

    @Override
    @NonNull
    default Multiple sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      this.getChannels()
          .forEach(channel -> channel.sendTitle(title, subtitle, fadeIn, stay, fadeOut));
      return this;
    }

    @Override
    @NonNull
    default Multiple setTabList(String header, String bottom) {
      this.getChannels().forEach(channel -> channel.setTabList(header, bottom));
      return this;
    }
  }
}
