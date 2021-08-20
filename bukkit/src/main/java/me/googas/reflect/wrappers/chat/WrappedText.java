package me.googas.reflect.wrappers.chat;

import lombok.NonNull;
import me.googas.reflect.APIVersion;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/** Class to wrap {@link Text} to not crash when older versions cannot use it. */
@APIVersion(since = 16)
public class WrappedText extends WrappedContent<Text> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedText(Text reference) {
    super(reference);
  }

  /**
   * Create the wrapper by initializing using {@link Text#Text(BaseComponent[])}.
   *
   * @param components the components of the text
   */
  public WrappedText(@NonNull BaseComponent[] components) {
    super(new Text(components));
  }

  @Override
  public @NonNull WrappedText set(@NonNull Text object) {
    return (WrappedText) super.set(object);
  }
}
