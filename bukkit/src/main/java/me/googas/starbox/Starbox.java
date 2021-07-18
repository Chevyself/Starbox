package me.googas.starbox;

import java.util.Objects;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;

public class Starbox {

  @Getter private static StarboxPlugin instance;

  public static void setInstance(StarboxPlugin instance) {
    if (Starbox.instance != null && instance != null)
      throw new IllegalStateException("Plugin is already initialized");
    Starbox.instance = instance;
  }

  @NonNull
  public static StarboxPlugin getPlugin() {
    return Objects.requireNonNull(Starbox.instance, "Starbox has not been initialized");
  }

  @NonNull
  public static Logger getLogger() {
    return Starbox.getPlugin().getLogger();
  }
}
