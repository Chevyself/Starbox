package me.googas.reflect.wrappers.book;

import lombok.NonNull;
import me.googas.reflect.APIVersion;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedMethod;
import me.googas.starbox.utility.Versions;

@APIVersion(12)
public enum WrappedBookMetaGeneration {
  ORIGINAL,
  COPY_OF_ORIGINAL,
  COPY_OF_COPY,
  TATTERED;

  @NonNull
  public static final WrappedClass GENERATION =
      WrappedClass.forName("org.bukkit.inventory.meta.BookMeta.Generation");

  @NonNull
  private static final WrappedMethod VALUE_OF =
      WrappedBookMetaGeneration.GENERATION.getMethod("valueOf");

  @NonNull
  public Object toGeneration() {
    if (Versions.BUKKIT >= 12) {
      Object invoke = WrappedBookMetaGeneration.VALUE_OF.invoke(null, this.name());
      if (invoke != null) return invoke;
    }
    throw new IllegalStateException(
        "Attempted to get attribute in an illegal version of Minecraft");
  }
}
