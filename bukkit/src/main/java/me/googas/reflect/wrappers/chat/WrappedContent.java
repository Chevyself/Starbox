package me.googas.reflect.wrappers.chat;

import me.googas.reflect.StarboxWrapper;
import net.md_5.bungee.api.chat.hover.content.Content;

/**
 * Class to wrap {@link net.md_5.bungee.api.chat.hover.content.Content} to not crash when older
 * versions cannot use it.
 *
 * @param <C> any type of {@link Content}
 */
public class WrappedContent<C extends Content> extends StarboxWrapper<C> {
  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedContent(C reference) {
    super(reference);
  }
}
