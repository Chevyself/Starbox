package me.googas.starbox.modules.channels;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;

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
    default Optional<Locale> getLocale() {
      return Optional.empty();
    }
  }
}
