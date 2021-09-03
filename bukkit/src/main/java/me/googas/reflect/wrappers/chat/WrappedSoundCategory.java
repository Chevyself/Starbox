package me.googas.reflect.wrappers.chat;

import lombok.NonNull;
import me.googas.reflect.StarboxWrapper;
import org.bukkit.SoundCategory;

/** Class to wrap {@link SoundCategory} to not crash when older versions cannot use it. */
public class WrappedSoundCategory extends StarboxWrapper<SoundCategory> {
  @NonNull public static final WrappedSoundCategory MASTER = WrappedSoundCategory.valueOf("MASTER");
  @NonNull public static final WrappedSoundCategory MUSIC = WrappedSoundCategory.valueOf("MUSIC");

  @NonNull
  public static final WrappedSoundCategory RECORDS = WrappedSoundCategory.valueOf("RECORDS");

  @NonNull
  public static final WrappedSoundCategory WEATHER = WrappedSoundCategory.valueOf("WEATHER");

  @NonNull public static final WrappedSoundCategory BLOCKS = WrappedSoundCategory.valueOf("BLOCKS");

  @NonNull
  public static final WrappedSoundCategory HOSTILE = WrappedSoundCategory.valueOf("HOSTILE");

  @NonNull
  public static final WrappedSoundCategory NEUTRAL = WrappedSoundCategory.valueOf("NEUTRAL");

  @NonNull
  public static final WrappedSoundCategory PLAYERS = WrappedSoundCategory.valueOf("PLAYERS");

  @NonNull
  public static final WrappedSoundCategory AMBIENT = WrappedSoundCategory.valueOf("AMBIENT");

  @NonNull public static final WrappedSoundCategory VOICE = WrappedSoundCategory.valueOf("VOICE");

  /**
   * Create the wrapper.
   *
   * @param reference the reference of the wrapper
   */
  WrappedSoundCategory(@NonNull SoundCategory reference) {
    super(reference);
  }

  /**
   * Get a sound category by its name.
   *
   * @param name the name to match
   * @return the wrapped sound category
   * @throws IllegalStateException if the name does not match
   */
  @NonNull
  public static WrappedSoundCategory valueOf(@NonNull String name) {
    return new WrappedSoundCategory(SoundCategory.valueOf(name));
  }
}
