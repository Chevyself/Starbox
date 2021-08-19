package me.googas.reflect.wrappers.chat;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/** Class to wrap {@link Text} to not crash when older versions cannot use it. */
@APIVersion(since = 16)
public class WrappedText extends StarboxWrapper<Text> {

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

  /**
   * Get the actual text checking that is not null.
   *
   * @return the text
   * @throws NullPointerException if there's no text
   */
  @NonNull
  @Delegate
  public Text getText() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedText set(@NonNull Text object) {
    return (WrappedText) super.set(object);
  }
}
