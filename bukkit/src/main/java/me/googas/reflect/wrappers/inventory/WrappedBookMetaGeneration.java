package me.googas.reflect.wrappers.inventory;

import lombok.NonNull;
import lombok.experimental.Delegate;
import me.googas.reflect.APIVersion;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.inventory.meta.BookMeta;

/** Class to wrap {@link BookMeta.Generation} to not crash when older versions cannot use it. */
@APIVersion(since = 9)
public class WrappedBookMetaGeneration extends StarboxWrapper<BookMeta.Generation> {

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  public WrappedBookMetaGeneration(@NonNull BookMeta.Generation reference) {
    super(reference);
  }

  /**
   * Get the actual book generation checking that is not null.
   *
   * @return the generation
   * @throws NullPointerException if there's no generation
   */
  @NonNull
  @Delegate
  public BookMeta.Generation getGeneration() {
    return this.get().orElseThrow(NullPointerException::new);
  }

  @Override
  public @NonNull WrappedBookMetaGeneration set(@NonNull BookMeta.Generation object) {
    return (WrappedBookMetaGeneration) super.set(object);
  }
}
