package me.googas.starbox.addons.java;

import java.util.Objects;
import java.util.logging.Logger;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.StarboxFile;
import me.googas.starbox.addons.Addon;

/** This is the class that every addon made in Java can extend */
public class JavaAddon implements Addon {

  @NonNull private static final String NOT_INIT = "Addon has not been initialized yet!";

  @Setter private JavaAddonLoader addonLoader = null;
  @Setter private StarboxFile dataFile = null;
  @Setter private JavaAddonInformation information = null;
  @Setter private ClassLoader classLoader = null;
  @Setter private Logger logger = null;

  @NonNull
  public JavaAddon init(
      @NonNull JavaAddonLoader addonLoader,
      @NonNull StarboxFile dataFile,
      @NonNull JavaAddonInformation information,
      @NonNull ClassLoader classLoader,
      @NonNull Logger logger) {
    this.addonLoader = addonLoader;
    this.dataFile = dataFile;
    this.information = information;
    this.classLoader = classLoader;
    this.logger = logger;
    return this;
  }

  @NonNull
  public JavaAddonLoader getAddonLoader() {
    return Objects.requireNonNull(this.addonLoader, JavaAddon.NOT_INIT);
  }

  @NonNull
  public StarboxFile getDataFile() {
    return Objects.requireNonNull(this.dataFile, JavaAddon.NOT_INIT);
  }

  @NonNull
  public ClassLoader getClassLoader() {
    return Objects.requireNonNull(this.classLoader, JavaAddon.NOT_INIT);
  }

  @NonNull
  public Logger getLogger() {
    return Objects.requireNonNull(this.logger, JavaAddon.NOT_INIT);
  }

  @Override
  @NonNull
  public JavaAddonInformation getInformation() {
    return Objects.requireNonNull(this.information, JavaAddon.NOT_INIT);
  }

  public void onEnable() throws Throwable {}

  public void onDisable() throws Throwable {}
}
